import { api, ApiError } from "../core/api.js";
import { session } from "../core/session.js";
import { renderTable } from "../components/table.js";
import { openModal } from "../components/modal.js";
import { showToast } from "../components/toast.js";
import { validateRequired } from "../components/form-validator.js";

export async function renderWaste(container) {
  const isGestor = session.isGestor();

  container.innerHTML = `
    <div class="view-header">
      <div>
        <span class="eyebrow">Residuos</span>
        <h1>Tipos de residuo</h1>
      </div>
      ${isGestor ? '<button class="btn" type="button" data-new-waste>Nuevo tipo</button>' : ""}
    </div>
    <div data-waste-table></div>
  `;

  async function load() {
    const tableWrap = container.querySelector("[data-waste-table]");
    tableWrap.innerHTML = '<p class="view-empty">Cargando…</p>';
    try {
      const waste = await api.get("/api/waste");
      tableWrap.innerHTML = "";
      tableWrap.appendChild(
        renderTable({
          columns: [
            { label: "Tipo", key: "type" },
            { label: "Peso (kg)", render: (w) => (w.weight != null ? `${w.weight} kg` : "—") },
          ],
          rows: waste,
          emptyMessage: "Aún no hay tipos de residuo registrados.",
          getActions: isGestor
            ? (item) => [
                { label: "Editar", variant: "ghost", onClick: () => openForm(item) },
                { label: "Eliminar", variant: "danger", onClick: () => remove(item) },
              ]
            : undefined,
        })
      );
    } catch (error) {
      tableWrap.innerHTML = '<p class="view-empty">No se pudieron cargar los tipos de residuo.</p>';
      showToast(error.message, { variant: "error" });
    }
  }

  async function remove(item) {
    if (!confirm(`¿Eliminar el tipo de residuo "${item.type}"?`)) return;
    try {
      await api.del(`/api/waste/id/${item.id}`);
      showToast("Tipo de residuo eliminado.");
      load();
    } catch (error) {
      showToast(error.message, { variant: "error" });
    }
  }

  function openForm(item) {
    const isEdit = Boolean(item);
    const body = document.createElement("div");
    body.innerHTML = `
      <form data-waste-form novalidate>
        <div class="form-field">
          <label for="waste-type">Tipo</label>
          <input id="waste-type" name="type" value="${item?.type ?? ""}" required />
        </div>
        <div class="form-field">
          <label for="waste-weight">Peso (kg)</label>
          <input id="waste-weight" name="weight" type="number" step="0.01" min="0" value="${item?.weight ?? ""}" required />
        </div>
        <div class="form-actions">
          <button class="btn" type="submit">${isEdit ? "Guardar cambios" : "Crear tipo"}</button>
        </div>
      </form>
    `;

    const { close } = openModal({ title: isEdit ? "Editar tipo de residuo" : "Nuevo tipo de residuo", bodyEl: body });
    const form = body.querySelector("[data-waste-form]");

    form.addEventListener("submit", async (event) => {
      event.preventDefault();
      if (!validateRequired(form, ["type", "weight"])) return;

      const payload = {
        type: form.type.value.trim(),
        weight: Number(form.weight.value),
      };
      if (isEdit) payload.id = item.id;

      try {
        if (isEdit) {
          await api.put("/api/waste", payload);
          showToast("Tipo de residuo actualizado.");
        } else {
          await api.post("/api/waste", payload);
          showToast("Tipo de residuo creado.");
        }
        close();
        load();
      } catch (error) {
        const message = error instanceof ApiError ? error.message : "No se pudo guardar el tipo de residuo.";
        showToast(message, { variant: "error" });
      }
    });
  }

  if (isGestor) {
    container.querySelector("[data-new-waste]").addEventListener("click", () => openForm(null));
  }

  load();
}
