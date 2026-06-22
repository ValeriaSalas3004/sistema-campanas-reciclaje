import { api, ApiError } from "../core/api.js";
import { session } from "../core/session.js";
import { formatDate, campaignStatus, campaignTimeProgress, isUpcoming } from "../core/format.js";
import { renderTable } from "../components/table.js";
import { openModal } from "../components/modal.js";
import { showToast } from "../components/toast.js";
import { validateRequired } from "../components/form-validator.js";

export async function renderCampaigns(container) {
  const isGestor = session.isGestor();
  const isVoluntario = session.isVoluntario();
  const user = session.current();

  container.innerHTML = `
    <div class="view-header">
      <div>
        <span class="eyebrow">Campañas</span>
        <h1>Campañas de reciclaje</h1>
      </div>
      ${isGestor ? '<button class="btn" type="button" data-new-campaign>Nueva campaña</button>' : ""}
    </div>
    <div data-campaigns-table></div>
  `;

  async function load() {
    const tableWrap = container.querySelector("[data-campaigns-table]");
    tableWrap.innerHTML = '<p class="view-empty">Cargando…</p>';

    try {
      const campaigns = await api.get("/campaigns");
      let enrolledCampaignIds = new Map();
      if (isVoluntario) {
        const enrollments = await api.get(`/enrollments/user/${user.id}`);
        enrollments.forEach((e) => enrolledCampaignIds.set(e.campaign.id, e.id));
      }

      tableWrap.innerHTML = "";
      tableWrap.appendChild(
        renderTable({
          columns: [
            { label: "Título", key: "title" },
            {
              label: "Estado",
              render: (c) => {
                const status = campaignStatus(c);
                const badge = document.createElement("span");
                badge.className = `badge badge--${status.variant}`;
                badge.textContent = status.label;
                return badge;
              },
            },
            {
              label: "Periodo",
              render: (c) => `${formatDate(c.startDate)} → ${formatDate(c.endDate)}`,
            },
            {
              label: "Progreso del periodo",
              render: (c) => {
                const wrap = document.createElement("div");
                wrap.className = "campaign-progress";
                const bar = document.createElement("div");
                bar.className = "stream-progress";
                const fill = document.createElement("div");
                fill.className = "stream-progress__fill";
                const pct = campaignTimeProgress(c);
                fill.style.width = `${pct}%`;
                bar.appendChild(fill);
                const label = document.createElement("span");
                label.className = "campaign-progress__label";
                label.textContent = `${pct}%`;
                wrap.append(bar, label);
                return wrap;
              },
            },
          ],
          rows: campaigns,
          emptyMessage: "Aún no hay campañas registradas.",
          getActions: isGestor || isVoluntario ? (row) => buildActions(row, enrolledCampaignIds) : undefined,
        })
      );
    } catch (error) {
      tableWrap.innerHTML = '<p class="view-empty">No se pudieron cargar las campañas.</p>';
      showToast(error.message, { variant: "error" });
    }
  }

  function buildActions(campaign, enrolledMap) {
    const actions = [];

    if (isVoluntario && isUpcoming(campaign)) {
      const enrollmentId = enrolledMap.get(campaign.id);
      if (enrollmentId) {
        actions.push({
          label: "Cancelar inscripción",
          variant: "ghost",
          onClick: () => cancelEnrollment(enrollmentId),
        });
      } else {
        actions.push({
          label: "Inscribirme",
          onClick: () => enroll(campaign.id),
        });
      }
    }

    if (isGestor) {
      actions.push({ label: "Editar", variant: "warning", onClick: () => openForm(campaign) });
      actions.push({ label: "Eliminar", variant: "danger", onClick: () => remove(campaign) });
    }

    return actions;
  }

  async function enroll(campaignId) {
    try {
      await api.post("/enrollments", { userId: user.id, campaignId });
      showToast("Te inscribiste en la campaña.");
      load();
    } catch (error) {
      showToast(error.message, { variant: "error" });
    }
  }

  async function cancelEnrollment(enrollmentId) {
    try {
      await api.del(`/enrollments/${enrollmentId}`);
      showToast("Inscripción cancelada.");
      load();
    } catch (error) {
      showToast(error.message, { variant: "error" });
    }
  }

  async function remove(campaign) {
    if (!confirm(`¿Eliminar la campaña "${campaign.title}"?`)) return;
    try {
      await api.del(`/campaigns/${campaign.id}`);
      showToast("Campaña eliminada.");
      load();
    } catch (error) {
      showToast(error.message, { variant: "error" });
    }
  }

  function openForm(campaign) {
    const isEdit = Boolean(campaign);
    const body = document.createElement("div");
    body.innerHTML = `
      <form data-campaign-form novalidate>
        <div class="form-field">
          <label for="campaign-title">Título</label>
          <input id="campaign-title" name="title" value="${campaign?.title ?? ""}" required />
        </div>
        <div class="form-field">
          <label for="campaign-description">Descripción</label>
          <textarea id="campaign-description" name="description" rows="3">${campaign?.description ?? ""}</textarea>
        </div>
        <div class="form-grid">
          <div class="form-field">
            <label for="campaign-start">Fecha de inicio</label>
            <input id="campaign-start" name="startDate" type="date" value="${campaign?.startDate ?? ""}" required />
          </div>
          <div class="form-field">
            <label for="campaign-end">Fecha de finalización</label>
            <input id="campaign-end" name="endDate" type="date" value="${campaign?.endDate ?? ""}" required />
          </div>
        </div>
        <div class="form-actions">
          <button class="btn" type="submit">${isEdit ? "Guardar cambios" : "Crear campaña"}</button>
        </div>
      </form>
    `;

    const { close } = openModal({ title: isEdit ? "Editar campaña" : "Nueva campaña", bodyEl: body });
    const form = body.querySelector("[data-campaign-form]");

    form.addEventListener("submit", async (event) => {
      event.preventDefault();
      if (!validateRequired(form, ["title", "startDate", "endDate"])) return;

      const payload = {
        title: form.title.value.trim(),
        description: form.description.value.trim(),
        startDate: form.startDate.value,
        endDate: form.endDate.value,
      };

      try {
        if (isEdit) {
          await api.put(`/campaigns/${campaign.id}`, payload);
          showToast("Campaña actualizada.");
        } else {
          await api.post("/campaigns", payload);
          showToast("Campaña creada.");
        }
        close();
        load();
      } catch (error) {
        const message = error instanceof ApiError ? error.message : "No se pudo guardar la campaña.";
        showToast(message, { variant: "error" });
      }
    });
  }

  if (isGestor) {
    container.querySelector("[data-new-campaign]").addEventListener("click", () => openForm(null));
  }

  load();
}
