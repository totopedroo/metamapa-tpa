document.addEventListener("DOMContentLoaded", () => {
  const solicitudes = [
    {
      hecho: "Foco activo en cordón serrano",
      fecha: "2025-08-30",
      usuario: "Anónimo",
      justificacion: "Columna de humo visible desde ruta provincial.",
      estado: "Pendiente"
    },
    {
      hecho: "Derrame en arroyo local",
      fecha: "2025-08-28",
      usuario: "Anónimo",
      justificacion: "Vecinos reportan manchas y olor fuerte.",
      estado: "Pendiente"
    },
    {
      hecho: "Vertido industrial no autorizado",
      fecha: "2025-08-27",
      usuario: "Anónimo",
      justificacion: "Registro repetido por dos fuentes.",
      estado: "Pendiente"
    }
  ];

  const tbody = document.querySelector("#tablaSolicitudes tbody");

  solicitudes.forEach((s, index) => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${s.hecho}</td>
      <td>${s.fecha}</td>
      <td>${s.usuario}</td>
      <td>${s.justificacion}</td>
      <td><span class="chip ${s.estado.toLowerCase()}">${s.estado}</span></td>
      <td>
        <button class="btn btn-sm btn-aceptar me-1" data-index="${index}">Aceptar</button>
        <button class="btn btn-sm btn-rechazar" data-index="${index}">Rechazar</button>
      </td>
    `;
    tbody.appendChild(tr);
  });

  // Eventos botones aceptar/rechazar
  tbody.addEventListener("click", (e) => {
    const index = e.target.dataset.index;
    if(index === undefined) return;

    if(e.target.classList.contains("btn-aceptar")){
      solicitudes[index].estado = "Aprobada";
    } else if(e.target.classList.contains("btn-rechazar")){
      solicitudes[index].estado = "Rechazada";
    }

    // Actualizar chip
    const row = tbody.rows[index];
    const chip = row.querySelector(".chip");
    chip.textContent = solicitudes[index].estado.toUpperCase();
    chip.className = `chip ${solicitudes[index].estado.toLowerCase()}`;

    // Crear nuevo badge
    const badge = document.createElement("div");
    badge.className = "badge-confirm";
    badge.textContent = `¡${solicitudes[index].estado}!`;
    badgeContainer.appendChild(badge);

    // Forzar reflow para activar transición
    void badge.offsetWidth;
    badge.classList.add("show");

    // Quitar después de 2s
    setTimeout(() => {
      badge.classList.remove("show");
      setTimeout(() => badge.remove(), 300); // esperar transición
    }, 2000);
  });
});
