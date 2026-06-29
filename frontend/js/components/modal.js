export function openModal({ title, bodyEl, onClose } = {}) {
  const overlay = document.createElement("div");
  overlay.className = "modal-overlay";

  const modal = document.createElement("div");
  modal.className = "modal";

  const header = document.createElement("div");
  header.className = "modal__header";

  const heading = document.createElement("h2");
  heading.textContent = title;

  const closeBtn = document.createElement("button");
  closeBtn.type = "button";
  closeBtn.className = "modal__close";
  closeBtn.setAttribute("aria-label", "Cerrar");
  closeBtn.textContent = "✕";

  header.append(heading, closeBtn);
  modal.append(header, bodyEl);
  overlay.appendChild(modal);
  document.body.appendChild(overlay);

  function close() {
    overlay.remove();
    document.removeEventListener("keydown", onKeydown);
    if (onClose) onClose();
  }

  function onKeydown(event) {
    if (event.key === "Escape") close();
  }

  overlay.addEventListener("click", (event) => {
    if (event.target === overlay) close();
  });
  closeBtn.addEventListener("click", close);
  document.addEventListener("keydown", onKeydown);

  return { close };
}
