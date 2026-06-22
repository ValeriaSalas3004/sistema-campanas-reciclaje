const STORAGE_KEY = "recicla_session";

function read() {
  const raw = sessionStorage.getItem(STORAGE_KEY);
  return raw ? JSON.parse(raw) : null;
}

export const session = {
  hasEntered() {
    return read() !== null;
  },

  setUser(user) {
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify({ guest: false, ...user }));
  },

  setGuest() {
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify({ guest: true }));
  },

  clear() {
    sessionStorage.removeItem(STORAGE_KEY);
  },

  isGuest() {
    const s = read();
    return !s || s.guest === true;
  },

  current() {
    const s = read();
    return s && !s.guest ? s : null;
  },

  role() {
    const user = this.current();
    return user ? user.role : "invitado";
  },

  isGestor() {
    return this.role() === "gestor";
  },

  isVoluntario() {
    return this.role() === "voluntario";
  },
};
