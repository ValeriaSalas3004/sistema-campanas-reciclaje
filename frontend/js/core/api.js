import { session } from "./session.js";

export const API_BASE = "sistema-campanas-reciclaje.onrender.com";

export class ApiError extends Error {
  constructor(message, status) {
    super(message);
    this.status = status;
  }
}

function extractMessage(data) {
  if (typeof data === "string") return data;
  if (data && typeof data === "object") {
    const values = Object.values(data).filter((v) => typeof v === "string");
    if (values.length) return values.join(" ");
  }
  return "No se pudo completar la solicitud.";
}

async function request(path, { method = "GET", body } = {}) {
  const token = session.token();
  const headers = {
    ...(body !== undefined ? { "Content-Type": "application/json" } : {}),
    ...(token ? { Authorization: `Basic ${token}` } : {}),
  };

  let response;
  try {
    response = await fetch(`${API_BASE}${path}`, {
      method,
      headers: Object.keys(headers).length ? headers : undefined,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  } catch {
    throw new ApiError("No se pudo conectar con el servidor. Verifica que el backend esté en línea.", 0);
  }

  const text = await response.text();
  let data = null;
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!response.ok) {
    throw new ApiError(extractMessage(data), response.status);
  }
  return data;
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: "POST", body }),
  put: (path, body) => request(path, { method: "PUT", body }),
  del: (path) => request(path, { method: "DELETE" }),
};
