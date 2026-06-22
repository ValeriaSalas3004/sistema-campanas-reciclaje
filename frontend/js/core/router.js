import { session } from "./session.js";
import { renderDashboard } from "../views/dashboard.js";
import { renderCampaigns } from "../views/campaigns.js";
import { renderReports } from "../views/reports.js";
import { renderZones } from "../views/zones.js";
import { renderWaste } from "../views/waste.js";
import { renderUsers } from "../views/users.js";
import { renderProfile } from "../views/profile.js";

if (!session.hasEntered()) {
  window.location.href = "../index.html";
}

const routes = {
  "#/dashboard": { render: renderDashboard },
  "#/campaigns": { render: renderCampaigns },
  "#/reports": { render: renderReports },
  "#/zones": { render: renderZones },
  "#/waste": { render: renderWaste },
  "#/users": { render: renderUsers, gestorOnly: true },
  "#/profile": { render: renderProfile, requiresLogin: true },
};

function currentHash() {
  return location.hash || "#/dashboard";
}

function updateChrome() {
  const isLoggedIn = !session.isGuest();
  const isGestor = session.isGestor();
  const hash = currentHash();

  document.querySelectorAll("[data-nav]").forEach((link) => {
    link.classList.toggle("is-active", link.getAttribute("href") === hash);
  });

  document.querySelector("[data-nav-users]")?.classList.toggle("is-hidden", !isGestor);
  document.querySelector("[data-nav-profile]")?.classList.toggle("is-hidden", !isLoggedIn);

  const logoutBtn = document.querySelector("[data-logout]");
  if (logoutBtn) {
    logoutBtn.textContent = isLoggedIn ? "Cerrar sesión" : "Volver al login";
  }

  const summary = document.querySelector("[data-session-summary]");
  if (summary) {
    summary.textContent = isLoggedIn ? `${session.current().name} · ${session.role()}` : "Invitado";
  }
}

async function renderRoute() {
  const hash = currentHash();
  if (!location.hash) {
    history.replaceState(null, "", hash);
  }

  const route = routes[hash] ?? routes["#/dashboard"];
  const app = document.getElementById("app");

  if (route.gestorOnly && !session.isGestor()) {
    app.innerHTML = '<p class="view-empty">No tienes acceso a esta sección.</p>';
    updateChrome();
    return;
  }

  if (route.requiresLogin && session.isGuest()) {
    app.innerHTML = '<p class="view-empty">Debes iniciar sesión para ver esta sección.</p>';
    updateChrome();
    return;
  }

  app.innerHTML = "";
  await route.render(app);
  updateChrome();
}

document.querySelector("[data-logout]")?.addEventListener("click", () => {
  session.clear();
  window.location.href = "../index.html";
});

window.addEventListener("hashchange", renderRoute);
renderRoute();
