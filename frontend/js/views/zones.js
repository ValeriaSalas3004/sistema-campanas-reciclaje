import { api, ApiError } from "../core/api.js";
import { session } from "../core/session.js";
import { renderTable } from "../components/table.js";
import { openModal } from "../components/modal.js";
import { showToast } from "../components/toast.js";
import { validateRequired } from "../components/form-validator.js";

export async function renderZones(container) {
  const isGestor = session.isGestor();

  container.innerHTML = `
    <div class="view-header">
      <div>
        <span class="eyebrow">Zonas</span>
        <h1>Zonas de recolección</h1>
      </div>
      ${isGestor ? '<button class="btn" type="button" data-new-zone>Nueva zona</button>' : ""}
    </div>
    <div data-zones-table></div>
  `;

  async function load() {
    const tableWrap = container.querySelector("[data-zones-table]");
    tableWrap.innerHTML = '<p class="view-empty">Cargando…</p>';
    try {
      const zones = await api.get("/api/zones");
      tableWrap.innerHTML = "";
      tableWrap.appendChild(
        renderTable({
          columns: [
            { label: "Ubicación", key: "location" },
            { label: "Horario", key: "schedule" },
          ],
          rows: zones,
          emptyMessage: "Aún no hay zonas registradas.",
          getActions: isGestor
            ? (zone) => [
                { label: "Editar", variant: "ghost", onClick: () => openForm(zone) },
                { label: "Eliminar", variant: "danger", onClick: () => remove(zone) },
              ]
            : undefined,
        })
      );
    } catch (error) {
      tableWrap.innerHTML = '<p class="view-empty">No se pudieron cargar las zonas.</p>';
      showToast(error.message, { variant: "error" });
    }
  }

  async function remove(zone) {
    if (!confirm(`¿Eliminar la zona "${zone.location}"?`)) return;
    try {
      await api.del(`/api/zones/${zone.id}`);
      showToast("Zona eliminada.");
      load();
    } catch (error) {
      showToast(error.message, { variant: "error" });
    }
  }

  function openForm(zone) {
    const isEdit = Boolean(zone);
    const body = document.createElement("div");
    body.innerHTML = `
      <form data-zone-form novalidate>
        <div class="form-field">
          <label for="zone-location">Ubicación</label>
          <input id="zone-location" name="location" value="${zone?.location ?? ""}" required />
        </div>
        <div class="form-field">
          <label for="zone-schedule">Horario</label>
          <input id="zone-schedule" name="schedule" value="${zone?.schedule ?? ""}" required />
        </div>
        <div class="form-actions">
          <button class="btn" type="submit">${isEdit ? "Guardar cambios" : "Crear zona"}</button>
        </div>
      </form>
    `;

    const { close } = openModal({ title: isEdit ? "Editar zona" : "Nueva zona", bodyEl: body });
    const form = body.querySelector("[data-zone-form]");

    form.addEventListener("submit", async (event) => {
      event.preventDefault();
      if (!validateRequired(form, ["location", "schedule"])) return;

      const payload = {
        location: form.location.value.trim(),
        schedule: form.schedule.value.trim(),
      };
      if (isEdit) payload.id = zone.id;

      try {
        if (isEdit) {
          await api.put("/api/zones", payload);
          showToast("Zona actualizada.");
        } else {
          await api.post("/api/zones", payload);
          showToast("Zona creada.");
        }
        close();
        load();
      } catch (error) {
        const message = error instanceof ApiError ? error.message : "No se pudo guardar la zona.";
        showToast(message, { variant: "error" });
      }
    });
  }

  if (isGestor) {
    container.querySelector("[data-new-zone]").addEventListener("click", () => openForm(null));
  }

  load();
}
