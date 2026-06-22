export function showToast(message, { variant = "info" } = {}) {
  const stack = document.querySelector("[data-toast-stack]");
  if (!stack) return;

  const toast = document.createElement("div");
  toast.className = `toast${variant === "error" ? " toast--error" : ""}`;
  toast.textContent = message;
  stack.appendChild(toast);

  setTimeout(() => toast.remove(), 4000);
}
