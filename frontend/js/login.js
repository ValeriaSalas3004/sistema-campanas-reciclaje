import { api, ApiError } from "./core/api.js";
import { session } from "./core/session.js";
import { showToast } from "./components/toast.js";
import { validateRequired } from "./components/form-validator.js";

if (session.hasEntered()) {
  window.location.href = "pages/dashboard.html";
}

const form = document.getElementById("login-form");

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!validateRequired(form, ["email", "password"])) return;

  const email = form.email.value.trim();
  const password = form.password.value;

  try {
    const user = await api.post("/api/user/login", { email, password });
    const token = btoa(`${email}:${password}`);
    session.setUser(user, token);
    window.location.href = "pages/dashboard.html";
  } catch (error) {
    const message = error instanceof ApiError ? error.message : "No se pudo iniciar sesión.";
    showToast(message, { variant: "error" });
  }
});

document.getElementById("btn-guest").addEventListener("click", () => {
  session.setGuest();
  window.location.href = "pages/dashboard.html";
});
