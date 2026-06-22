import { api, ApiError } from "../core/api.js";
import { session } from "../core/session.js";
import { renderTable } from "../components/table.js";
import { openModal } from "../components/modal.js";
import { showToast } from "../components/toast.js";
import { validateRequired } from "../components/form-validator.js";

export async function renderUsers(container) {
  const currentUser = session.current();

  container.innerHTML = `
    <div class="view-header">
      <div>
        <span class="eyebrow">Administración</span>
        <h1>Gestión de usuarios</h1>
      </div>
      <button class="btn" type="button" data-new-user>Nuevo usuario</button>
    </div>
    <div data-users-table></div>
  `;

  async function load() {
    const tableWrap = container.querySelector("[data-users-table]");
    tableWrap.innerHTML = '<p class="view-empty">Cargando…</p>';
    try {
      const users = await api.get("/api/user");
      tableWrap.innerHTML = "";
      tableWrap.appendChild(
        renderTable({
          columns: [
            { label: "Nombre", key: "name" },
            { label: "Correo", key: "email" },
            {
              label: "Rol",
              render: (u) => {
                const badge = document.createElement("span");
                badge.className = `badge badge--${u.role === "gestor" ? "success" : "muted"}`;
                badge.textContent = u.role;
                return badge;
              },
            },
          ],
          rows: users,
          emptyMessage: "Aún no hay usuarios registrados.",
          getActions: (u) => {
            const actions = [{ label: "Editar", variant: "warning", onClick: () => openForm(u) }];
            if (u.id !== currentUser.id) {
              actions.push({ label: "Eliminar", variant: "danger", onClick: () => remove(u) });
            }
            return actions;
          },
        })
      );
    } catch (error) {
      tableWrap.innerHTML = '<p class="view-empty">No se pudieron cargar los usuarios.</p>';
      showToast(error.message, { variant: "error" });
    }
  }

  async function remove(user) {
    if (!confirm(`¿Eliminar al usuario "${user.name}"?`)) return;
    try {
      await api.del(`/api/user/${user.id}`);
      showToast("Usuario eliminado.");
      load();
    } catch (error) {
      showToast(error.message, { variant: "error" });
    }
  }

  function openForm(user) {
    const isEdit = Boolean(user);
    const body = document.createElement("div");
    body.innerHTML = `
      <form data-user-form novalidate>
        <div class="form-field">
          <label for="user-name">Nombre</label>
          <input id="user-name" name="name" value="${user?.name ?? ""}" required />
        </div>
        <div class="form-field">
          <label for="user-email">Correo</label>
          <input id="user-email" name="email" type="email" value="${user?.email ?? ""}" required />
        </div>
        <div class="form-field">
          <label for="user-password">Contraseña</label>
          <input id="user-password" name="password" type="password" ${isEdit ? "" : "required"} />
          ${isEdit ? '<span class="eyebrow">Déjalo en blanco para mantener la actual.</span>' : ""}
        </div>
        <div class="form-field">
          <label for="user-role">Rol</label>
          <select id="user-role" name="role" required>
            <option value="gestor" ${user?.role === "gestor" ? "selected" : ""}>Gestor</option>
            <option value="voluntario" ${user?.role === "voluntario" || !user ? "selected" : ""}>Voluntario</option>
          </select>
        </div>
        <div class="form-actions">
          <button class="btn" type="submit">${isEdit ? "Guardar cambios" : "Crear usuario"}</button>
        </div>
      </form>
    `;

    const { close } = openModal({ title: isEdit ? "Editar usuario" : "Nuevo usuario", bodyEl: body });
    const form = body.querySelector("[data-user-form]");

    form.addEventListener("submit", async (event) => {
      event.preventDefault();
      const required = isEdit ? ["name", "email"] : ["name", "email", "password"];
      if (!validateRequired(form, required)) return;

      const payload = {
        name: form.name.value.trim(),
        email: form.email.value.trim(),
        password: form.password.value,
        role: form.role.value,
      };

      try {
        if (isEdit) {
          await api.put(`/api/user/${user.id}`, payload);
          showToast("Usuario actualizado.");
        } else {
          await api.post("/api/user", payload);
          showToast("Usuario creado.");
        }
        close();
        load();
      } catch (error) {
        const message = error instanceof ApiError ? error.message : "No se pudo guardar el usuario.";
        showToast(message, { variant: "error" });
      }
    });
  }

  container.querySelector("[data-new-user]").addEventListener("click", () => openForm(null));

  load();
}
