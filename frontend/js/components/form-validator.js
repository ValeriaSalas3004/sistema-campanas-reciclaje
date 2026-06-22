export function clearFieldErrors(form) {
  form.querySelectorAll(".form-field__error").forEach((el) => el.remove());
}

export function showFieldError(form, fieldName, message) {
  const field = form.querySelector(`[name="${fieldName}"]`);
  if (!field) return;
  const wrap = field.closest(".form-field") || field.parentElement;
  const error = document.createElement("span");
  error.className = "form-field__error";
  error.textContent = message;
  wrap.appendChild(error);
}

export function validateRequired(form, fieldNames) {
  clearFieldErrors(form);
  let isValid = true;
  fieldNames.forEach((name) => {
    const field = form.querySelector(`[name="${name}"]`);
    if (field && !String(field.value).trim()) {
      showFieldError(form, name, "Este campo es obligatorio.");
      isValid = false;
    }
  });
  return isValid;
}
