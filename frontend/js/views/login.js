import { api, ApiError } from "../core/api.js";
import { session } from "../core/session.js";
import { showToast } from "../components/toast.js";
import { validateRequired } from "../components/form-validator.js";

export async function renderLogin(container) {
  container.innerHTML = `
    <div class="login-view">
      <div class="login-card">
        <span class="eyebrow">Recicla UCR</span>
        <h1>Inicia sesión</h1>
        <p>Gestores y voluntarios acceden con su correo y contraseña.</p>

        <form data-login-form novalidate>
          <div class="form-field">
            <label for="login-email">Correo</label>
            <input id="login-email" name="email" type="email" autocomplete="username" required />
          </div>
          <div class="form-field">
            <label for="login-password">Contraseña</label>
            <input id="login-password" name="password" type="password" autocomplete="current-password" required />
          </div>
          <div class="form-actions">
            <button class="btn" type="submit">Iniciar sesión</button>
          </div>
        </form>

        <div class="login-divider">o</div>

        <button class="btn btn--ghost" type="button" data-guest-btn style="width: 100%;">
          Continuar como invitado
        </button>
        <p class="login-guest-hint">Podrás ver campañas, reportes, zonas y tipos de residuo sin iniciar sesión.</p>
      </div>
    </div>
  `;

  const form = container.querySelector("[data-login-form]");
  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (!validateRequired(form, ["email", "password"])) return;

    const email = form.email.value.trim();
    const password = form.password.value;

    try {
      const user = await api.post("/api/user/login", { email, password });
      session.setUser(user);
      showToast(`Bienvenido, ${user.name}.`);
      location.hash = "#/dashboard";
    } catch (error) {
      const message = error instanceof ApiError ? error.message : "No se pudo iniciar sesión.";
      showToast(message, { variant: "error" });
    }
  });

  container.querySelector("[data-guest-btn]").addEventListener("click", () => {
    session.setGuest();
    showToast("Estás navegando como invitado: solo podrás ver datos.");
    location.hash = "#/dashboard";
  });
}
