export function formatDate(isoDate) {
  if (!isoDate) return "—";
  const [year, month, day] = isoDate.split("-");
  return `${day}/${month}/${year}`;
}

export function campaignStatus(campaign) {
  const today = new Date().toISOString().slice(0, 10);
  if (campaign.startDate && today < campaign.startDate) {
    return { label: "Próxima", variant: "success" };
  }
  if (campaign.endDate && today > campaign.endDate) {
    return { label: "Finalizada", variant: "muted" };
  }
  return { label: "En curso", variant: "warning" };
}

export function campaignTimeProgress(campaign) {
  if (!campaign.startDate || !campaign.endDate) return 0;
  const start = new Date(campaign.startDate).getTime();
  const end = new Date(campaign.endDate).getTime();
  const now = Date.now();
  if (now <= start) return 0;
  if (now >= end) return 100;
  return Math.round(((now - start) / (end - start)) * 100);
}

export function isUpcoming(campaign) {
  const today = new Date().toISOString().slice(0, 10);
  return Boolean(campaign.startDate && today < campaign.startDate);
}
