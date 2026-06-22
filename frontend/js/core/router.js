import { session } from "./session.js";
import { renderLogin } from "../views/login.js";
import { renderDashboard } from "../views/dashboard.js";
import { renderCampaigns } from "../views/campaigns.js";
import { renderReports } from "../views/reports.js";
import { renderZones } from "../views/zones.js";
import { renderWaste } from "../views/waste.js";
import { renderUsers } from "../views/users.js";
import { renderProfile } from "../views/profile.js";

const routes = {
  "#/login": { render: renderLogin, public: true },
  "#/dashboard": { render: renderDashboard },
  "#/campaigns": { render: renderCampaigns },
  "#/reports": { render: renderReports },
  "#/zones": { render: renderZones },
  "#/waste": { render: renderWaste },
  "#/users": { render: renderUsers, gestorOnly: true },
  "#/profile": { render: renderProfile, requiresLogin: true },
};

function currentHash() {
  if (location.hash) return location.hash;
  return session.hasEntered() ? "#/dashboard" : "#/login";
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
  document.querySelector("[data-logout]")?.classList.toggle("is-hidden", !isLoggedIn);

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
    location.hash = "#/login";
    return;
  }

  if (!route.public && !session.hasEntered()) {
    location.hash = "#/login";
    return;
  }

  app.innerHTML = "";
  await route.render(app);
  updateChrome();
}

document.querySelector("[data-logout]")?.addEventListener("click", () => {
  session.clear();
  location.hash = "#/login";
});

window.addEventListener("hashchange", renderRoute);
renderRoute();
