export function renderTable({ columns, rows, getActions, emptyMessage = "No hay registros para mostrar." }) {
  const wrap = document.createElement("div");
  wrap.className = "data-table-wrap";

  if (!rows.length) {
    const empty = document.createElement("p");
    empty.className = "view-empty";
    empty.textContent = emptyMessage;
    wrap.appendChild(empty);
    return wrap;
  }

  const table = document.createElement("table");
  table.className = "data-table";

  const thead = document.createElement("thead");
  const headRow = document.createElement("tr");
  columns.forEach((col) => {
    const th = document.createElement("th");
    th.textContent = col.label;
    headRow.appendChild(th);
  });
  if (getActions) {
    const th = document.createElement("th");
    th.textContent = "Acciones";
    headRow.appendChild(th);
  }
  thead.appendChild(headRow);

  const tbody = document.createElement("tbody");
  rows.forEach((row) => {
    const tr = document.createElement("tr");
    columns.forEach((col) => {
      const td = document.createElement("td");
      const value = col.render ? col.render(row) : row[col.key];
      if (value instanceof HTMLElement) {
        td.appendChild(value);
      } else {
        td.textContent = value ?? "—";
      }
      tr.appendChild(td);
    });

    if (getActions) {
      const td = document.createElement("td");
      const actionsWrap = document.createElement("div");
      actionsWrap.className = "data-table__actions";
      getActions(row).forEach((action) => {
        const btn = document.createElement("button");
        btn.type = "button";
        btn.className = `btn btn--sm ${action.variant ? `btn--${action.variant}` : ""}`.trim();
        btn.textContent = action.label;
        btn.addEventListener("click", action.onClick);
        actionsWrap.appendChild(btn);
      });
      td.appendChild(actionsWrap);
      tr.appendChild(td);
    }

    tbody.appendChild(tr);
  });

  table.append(thead, tbody);
  wrap.appendChild(table);
  return wrap;
}
