import { api, ApiError } from "../core/api.js";
import { session } from "../core/session.js";
import { formatDate } from "../core/format.js";
import { renderTable } from "../components/table.js";
import { openModal } from "../components/modal.js";
import { showToast } from "../components/toast.js";
import { validateRequired } from "../components/form-validator.js";

export async function renderReports(container) {
  const isGestor = session.isGestor();
  const isVoluntario = session.isVoluntario();
  const user = session.current();

  container.innerHTML = `
    <div class="view-header">
      <div>
        <span class="eyebrow">Reportes</span>
        <h1>Reportes de recolección</h1>
      </div>
      ${isGestor || isVoluntario ? '<button class="btn" type="button" data-new-report>Nuevo reporte</button>' : ""}
    </div>
    <div class="reports-filters">
      <select data-filter-campaign><option value="">Todas las campañas</option></select>
      <select data-filter-zone><option value="">Todas las zonas</option></select>
      <select data-filter-waste><option value="">Todos los residuos</option></select>
    </div>
    <div data-reports-table></div>
  `;

  let state = { reports: [], campaigns: [], zones: [], waste: [], users: [] };

  function canEdit(report) {
    if (isGestor) return true;
    if (isVoluntario) return report.user?.id === user.id;
    return false;
  }

  function populateSelect(select, items, labelFn) {
    items.forEach((item) => {
      const option = document.createElement("option");
      option.value = item.id;
      option.textContent = labelFn(item);
      select.appendChild(option);
    });
  }

  function applyFilters() {
    const campaignId = container.querySelector("[data-filter-campaign]").value;
    const zoneId = container.querySelector("[data-filter-zone]").value;
    const wasteId = container.querySelector("[data-filter-waste]").value;

    return state.reports.filter((r) => {
      if (campaignId && String(r.campaign?.id) !== campaignId) return false;
      if (zoneId && String(r.recoZone?.id) !== zoneId) return false;
      if (wasteId && String(r.wasteType?.id) !== wasteId) return false;
      return true;
    });
  }

  function renderRows() {
    const tableWrap = container.querySelector("[data-reports-table]");
    const rows = applyFilters();
    tableWrap.innerHTML = "";
    tableWrap.appendChild(
      renderTable({
        columns: [
          { label: "Fecha", render: (r) => formatDate(r.reportDate) },
          { label: "Usuario", render: (r) => r.user?.name ?? "—" },
          { label: "Campaña", render: (r) => r.campaign?.title ?? "—" },
          { label: "Zona", render: (r) => r.recoZone?.location ?? "—" },
          { label: "Residuo", render: (r) => r.wasteType?.type ?? "—" },
        ],
        rows,
        emptyMessage: "No hay reportes para los filtros seleccionados.",
        getActions:
          isGestor || isVoluntario
            ? (report) =>
                canEdit(report)
                  ? [
                      { label: "Editar", variant: "warning", onClick: () => openForm(report) },
                      { label: "Eliminar", variant: "danger", onClick: () => remove(report) },
                    ]
                  : []
            : undefined,
      })
    );
  }

  async function load() {
    const tableWrap = container.querySelector("[data-reports-table]");
    tableWrap.innerHTML = '<p class="view-empty">Cargando…</p>';
    try {
      const [reports, campaigns, zones, waste, users] = await Promise.all([
        api.get("/api/reports"),
        api.get("/api/campaigns"),
        api.get("/api/zones"),
        api.get("/api/waste"),
        isGestor ? api.get("/api/user") : Promise.resolve([]),
      ]);
      state = { reports, campaigns, zones, waste, users };

      populateSelect(container.querySelector("[data-filter-campaign]"), campaigns, (c) => c.title);
      populateSelect(container.querySelector("[data-filter-zone]"), zones, (z) => z.location);
      populateSelect(container.querySelector("[data-filter-waste]"), waste, (w) => w.type);

      renderRows();
    } catch (error) {
      tableWrap.innerHTML = '<p class="view-empty">No se pudieron cargar los reportes.</p>';
      showToast(error.message, { variant: "error" });
    }
  }

  async function remove(report) {
    if (!confirm("¿Eliminar este reporte?")) return;
    try {
      await api.del(`/api/reports/${report.id}`);
      showToast("Reporte eliminado.");
      load();
    } catch (error) {
      showToast(error.message, { variant: "error" });
    }
  }

  function optionsMarkup(items, labelFn, selectedId) {
    return items
      .map((item) => `<option value="${item.id}" ${item.id === selectedId ? "selected" : ""}>${labelFn(item)}</option>`)
      .join("");
  }

  function openForm(report) {
    const isEdit = Boolean(report);
    const body = document.createElement("div");
    body.innerHTML = `
      <form data-report-form novalidate>
        <div class="form-field">
          <label for="report-date">Fecha</label>
          <input id="report-date" name="reportDate" type="date" value="${report?.reportDate ?? ""}" required />
        </div>
        ${
          isGestor
            ? `<div class="form-field">
                <label for="report-user">Usuario</label>
                <select id="report-user" name="userId" required>
                  <option value="">Selecciona…</option>
                  ${optionsMarkup(state.users, (u) => u.name, report?.user?.id)}
                </select>
              </div>`
            : ""
        }
        <div class="form-field">
          <label for="report-campaign">Campaña</label>
          <select id="report-campaign" name="campaignId" required>
            <option value="">Selecciona…</option>
            ${optionsMarkup(state.campaigns, (c) => c.title, report?.campaign?.id)}
          </select>
        </div>
        <div class="form-field">
          <label for="report-zone">Zona</label>
          <select id="report-zone" name="zoneId" required>
            <option value="">Selecciona…</option>
            ${optionsMarkup(state.zones, (z) => z.location, report?.recoZone?.id)}
          </select>
        </div>
        <div class="form-field">
          <label for="report-waste">Tipo de residuo</label>
          <select id="report-waste" name="wasteId" required>
            <option value="">Selecciona…</option>
            ${optionsMarkup(state.waste, (w) => w.type, report?.wasteType?.id)}
          </select>
        </div>
        <div class="form-actions">
          <button class="btn" type="submit">${isEdit ? "Guardar cambios" : "Crear reporte"}</button>
        </div>
      </form>
    `;

    const { close } = openModal({ title: isEdit ? "Editar reporte" : "Nuevo reporte", bodyEl: body });
    const form = body.querySelector("[data-report-form]");

    form.addEventListener("submit", async (event) => {
      event.preventDefault();
      const required = ["reportDate", "campaignId", "zoneId", "wasteId"];
      if (isGestor) required.push("userId");
      if (!validateRequired(form, required)) return;

      const payload = {
        reportDate: form.reportDate.value,
        user: { id: isGestor ? Number(form.userId.value) : user.id },
        campaign: { id: Number(form.campaignId.value) },
        recoZone: { id: Number(form.zoneId.value) },
        wasteType: { id: Number(form.wasteId.value) },
      };

      try {
        if (isEdit) {
          await api.put(`/api/reports/${report.id}`, payload);
          showToast("Reporte actualizado.");
        } else {
          await api.post("/api/reports", payload);
          showToast("Reporte creado.");
        }
        close();
        load();
      } catch (error) {
        const message = error instanceof ApiError ? error.message : "No se pudo guardar el reporte.";
        showToast(message, { variant: "error" });
      }
    });
  }

  ["[data-filter-campaign]", "[data-filter-zone]", "[data-filter-waste]"].forEach((selector) => {
    container.addEventListener("change", (event) => {
      if (event.target.matches(selector)) renderRows();
    });
  });

  if (isGestor || isVoluntario) {
    container.querySelector("[data-new-report]").addEventListener("click", () => openForm(null));
  }

  load();
}
