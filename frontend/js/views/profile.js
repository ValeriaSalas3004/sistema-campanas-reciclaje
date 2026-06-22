import { api, ApiError } from "../core/api.js";
import { session } from "../core/session.js";
import { formatDate } from "../core/format.js";
import { renderTable } from "../components/table.js";
import { showToast } from "../components/toast.js";
import { validateRequired } from "../components/form-validator.js";

export async function renderProfile(container) {
  const user = session.current();
  const isVoluntario = session.isVoluntario();

  container.innerHTML = `
    <div class="view-header">
      <div>
        <span class="eyebrow">Mi cuenta</span>
        <h1>Mi perfil</h1>
      </div>
    </div>
    <div class="profile-grid">
      <div class="card">
        <h2>Datos personales</h2>
        <form data-profile-form novalidate>
          <div class="form-field">
            <label for="profile-name">Nombre</label>
            <input id="profile-name" name="name" value="${user.name}" required />
          </div>
          <div class="form-field">
            <label for="profile-email">Correo</label>
            <input id="profile-email" name="email" type="email" value="${user.email}" required />
          </div>
          <div class="form-field">
            <label for="profile-password">Nueva contraseña</label>
            <input id="profile-password" name="password" type="password" />
            <span class="eyebrow">Déjalo en blanco para mantener la actual.</span>
          </div>
          <div class="form-actions">
            <button class="btn" type="submit">Guardar cambios</button>
          </div>
        </form>
      </div>
      <div class="view-section" data-enrollments-section ${isVoluntario ? "" : "hidden"}>
        <h2>Mis inscripciones a campañas</h2>
        <div data-enrollments-table></div>
      </div>
    </div>
  `;

  const form = container.querySelector("[data-profile-form]");
  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (!validateRequired(form, ["name", "email"])) return;

    const payload = {
      name: form.name.value.trim(),
      email: form.email.value.trim(),
      password: form.password.value,
      role: user.role,
    };

    try {
      const updated = await api.put(`/api/user/${user.id}`, payload);
      session.setUser(updated);
      showToast("Perfil actualizado.");
    } catch (error) {
      const message = error instanceof ApiError ? error.message : "No se pudo actualizar el perfil.";
      showToast(message, { variant: "error" });
    }
  });

  if (!isVoluntario) return;

  async function loadEnrollments() {
    const tableWrap = container.querySelector("[data-enrollments-table]");
    tableWrap.innerHTML = '<p class="view-empty">Cargando…</p>';
    try {
      const enrollments = await api.get(`/enrollments/user/${user.id}`);
      tableWrap.innerHTML = "";
      tableWrap.appendChild(
        renderTable({
          columns: [
            { label: "Campaña", render: (e) => e.campaign?.title ?? "—" },
            { label: "Inscrito desde", render: (e) => formatDate(e.enrolledAt) },
          ],
          rows: enrollments,
          emptyMessage: "Aún no te has inscrito a ninguna campaña.",
          getActions: (e) => [
            {
              label: "Cancelar inscripción",
              variant: "danger",
              onClick: async () => {
                try {
                  await api.del(`/enrollments/${e.id}`);
                  showToast("Inscripción cancelada.");
                  loadEnrollments();
                } catch (error) {
                  showToast(error.message, { variant: "error" });
                }
              },
            },
          ],
        })
      );
    } catch (error) {
      tableWrap.innerHTML = '<p class="view-empty">No se pudieron cargar tus inscripciones.</p>';
      showToast(error.message, { variant: "error" });
    }
  }

  loadEnrollments();
}
