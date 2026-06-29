import { api } from "../core/api.js";
import { session } from "../core/session.js";
import { formatDate, campaignStatus } from "../core/format.js";
import { showToast } from "../components/toast.js";

export async function renderDashboard(container) {
  const user = session.current();

  container.innerHTML = `
    <div class="view-header">
      <div>
        <span class="eyebrow">Resumen</span>
        <h1>${user ? `Hola, ${user.name}` : "Vista de invitado"}</h1>
      </div>
    </div>
    <div class="card-grid view-section" data-stats>
      <p class="view-empty">Cargando…</p>
    </div>
    <div class="view-section">
      <h2>Próximas campañas</h2>
      <div data-upcoming></div>
    </div>
  `;

  try {
    const [campaigns, reports, zones, waste] = await Promise.all([
      api.get("/api/campaigns"),
      api.get("/api/reports"),
      api.get("/api/zones"),
      api.get("/api/waste"),
    ]);

    const statsWrap = container.querySelector("[data-stats]");
    statsWrap.innerHTML = "";
    [
      ["Campañas", campaigns.length],
      ["Reportes", reports.length],
      ["Zonas de recolección", zones.length],
      ["Tipos de residuo", waste.length],
    ].forEach(([label, value]) => {
      const card = document.createElement("div");
      card.className = "card dashboard-card";
      card.innerHTML = `<span class="eyebrow">${label}</span><strong>${value}</strong>`;
      statsWrap.appendChild(card);
    });

    const upcoming = campaigns
      .filter((c) => campaignStatus(c).label === "Próxima")
      .sort((a, b) => (a.startDate > b.startDate ? 1 : -1))
      .slice(0, 5);

    const upcomingWrap = container.querySelector("[data-upcoming]");
    if (!upcoming.length) {
      upcomingWrap.innerHTML = '<p class="view-empty">No hay campañas próximas registradas.</p>';
    } else {
      const list = document.createElement("ul");
      list.className = "card-grid";
      upcoming.forEach((c) => {
        const item = document.createElement("li");
        item.className = "card";
        item.innerHTML = `
          <span class="eyebrow">${formatDate(c.startDate)} → ${formatDate(c.endDate)}</span>
          <h3>${c.title}</h3>
          <p>${c.description ?? ""}</p>
        `;
        list.appendChild(item);
      });
      upcomingWrap.appendChild(list);
    }
  } catch (error) {
    container.querySelector("[data-stats]").innerHTML =
      '<p class="view-empty">No se pudieron cargar los datos.</p>';
    showToast(error.message ?? "No se pudieron cargar los datos.", { variant: "error" });
  }
}
