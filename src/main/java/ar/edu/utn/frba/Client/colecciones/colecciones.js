document.addEventListener("DOMContentLoaded", () => {
  const rol = (localStorage.getItem("rol") || "usuario").toLowerCase();

  const btnVolver = document.querySelector(".btn-volver");
  const btnAgregar = document.querySelector(".title-section button");
  const logoutBtn = document.querySelector("a.logout-btn:not(.btn-invitado-header)");
  const invitadosBtn = document.querySelector(".btn-invitado-header");

  // Enlaces del header
  const linkHechos = document.querySelector("nav a[href*='hechos.html']");
  const linkColecciones = document.querySelector("nav a[href*='colecciones.html']");

  if (rol === "invitado") {
    if (btnVolver) btnVolver.style.display = "none";
    if (btnAgregar) btnAgregar.style.display = "none";
    if (logoutBtn) logoutBtn.style.display = "none";
    if (invitadosBtn) invitadosBtn.style.display = "inline-block";
  } else if (rol === "admin") {
    if (btnVolver) btnVolver.style.display = "inline-block";
    if (btnAgregar) btnAgregar.style.display = "inline-block";
    if (logoutBtn) logoutBtn.style.display = "inline-block";
    if (invitadosBtn) invitadosBtn.style.display = "none";

    // Ocultar Hechos y Colecciones en el header
    if (linkHechos) linkHechos.style.display = "none";
    if (linkColecciones) linkColecciones.style.display = "none";
  } else { // usuario normal
    if (btnVolver) btnVolver.style.display = "none";
    if (btnAgregar) btnAgregar.style.display = "none";
    if (logoutBtn) logoutBtn.style.display = "inline-block";
    if (invitadosBtn) invitadosBtn.style.display = "none";
  }

  // Render tabla según rol
  renderTabla();
});

function renderTabla() {
  const rol = (localStorage.getItem("rol") || "usuario").toLowerCase();
  const tbody = document.querySelector("#tablaColecciones tbody");
  tbody.innerHTML = "";

  colecciones.forEach((c, i) => {
    const row = document.createElement("tr");

    const verBtn = `
      <button class="btn btn-sm btn-success me-1"
        style="background-color: var(--primary); border: none;"
        onclick="window.location.href='../hechos/hechos.html'">
        VER HECHOS
      </button>
    `;

    let adminBtns = "";
    if (rol === "admin") {
      adminBtns = `
        <button class="btn btn-sm btn-warning me-1" onclick="editarColeccion(${i})">
          <i class="bi bi-pencil"></i>
        </button>
        <button class="btn btn-sm btn-danger" onclick="eliminarColeccion(${i})">
          <i class="bi bi-trash"></i>
        </button>
      `;
    }

    row.innerHTML = `
      <td>${escapeHtml(c.titulo)}</td>
      <td>${escapeHtml(c.descripcion)}</td>
      <td>${escapeHtml(c.hechos)}</td>
      <td>${escapeHtml(c.fuentes)}</td>
      <td>${escapeHtml(c.tag)}</td>
      <td>
        ${verBtn}
        ${adminBtns}
      </td>
    `;

    tbody.appendChild(row);
  });
}

function escapeHtml(text) {
  if (text === null || text === undefined) return "";
  return String(text)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}


// Colecciones hardcodeadas
let colecciones = [
  {
    titulo: "Incendios forestales en Argentina 2025",
    descripcion: "Seguimiento de focos activos y prevención.",
    hechos: 128,
    fuentes: 3,
    tag: "#incendios-arg-2025"
  },
  {
    titulo: "Desapariciones vinculadas a crímenes de odio",
    descripcion: "Registro y visibilización para impulsar acciones.",
    hechos: 56,
    fuentes: 4,
    tag: "#desapariciones-odio"
  },
  {
    titulo: "Deforestación NOA",
    descripcion: "Avance de la frontera agropecuaria.",
    hechos: 61,
    fuentes: 2,
    tag: "#deforestacion-noa"
  }
];

let editIndex = null;


// Modal Crear/Editar
function abrirModalColeccion() {
  editIndex = null;
  document.getElementById("modalColeccionTitle").textContent = "Nueva Colección";
  document.getElementById("formColeccion").reset();
  const modal = new bootstrap.Modal(document.getElementById("modalColeccion"));
  modal.show();
}

function editarColeccion(i) {
  editIndex = i;
  document.getElementById("modalColeccionTitle").textContent = "Editar Colección";
  document.getElementById("titulo").value = colecciones[i].titulo;
  document.getElementById("descripcion").value = colecciones[i].descripcion;
  document.getElementById("tag").value = colecciones[i].tag;
  const modal = new bootstrap.Modal(document.getElementById("modalColeccion"));
  modal.show();
}

function eliminarColeccion(i) {
  colecciones.splice(i, 1);
  renderTabla();
  mostrarBadge("Colección eliminada");
}

// Guardar
document.getElementById("formColeccion").addEventListener("submit", function(e) {
  e.preventDefault();

  const titulo = document.getElementById("titulo").value;
  const descripcion = document.getElementById("descripcion").value;
  const tag = document.getElementById("tag").value;

  if (editIndex === null) {
    colecciones.push({ titulo, descripcion, hechos: 0, fuentes: 0, tag });
    mostrarBadge("Colección creada");
  } else {
    colecciones[editIndex] = { ...colecciones[editIndex], titulo, descripcion, tag };
    mostrarBadge("Colección editada");
  }

  renderTabla();
  bootstrap.Modal.getInstance(document.getElementById("modalColeccion")).hide();
});

// Badge confirmación
function mostrarBadge(msg) {
  const badge = document.getElementById("badgeConfirm");
  badge.textContent = msg;
  badge.classList.add("show");
  setTimeout(() => badge.classList.remove("show"), 2000);
}

// Manejo de Tags con chips
  const tagInput = document.getElementById("tagInput");
  const tagContainer = document.getElementById("tagContainer");

  tagInput.addEventListener("keydown", function (event) {
    if (event.key === "Enter" && tagInput.value.trim() !== "") {
      event.preventDefault();

      const tagText = "#" + tagInput.value.trim();

      // Crear chip
      const chip = document.createElement("span");
      chip.className = "badge bg-success d-flex align-items-center";
      chip.style.gap = "6px";
      chip.innerHTML = `${tagText} <span style="cursor:pointer;">&times;</span>`;

      // Quitar chip al hacer click en la cruz
      chip.querySelector("span").addEventListener("click", () => {
        tagContainer.removeChild(chip);
      });

      tagContainer.appendChild(chip);
      tagInput.value = "";
    }
  });

// Inicializar
renderTabla();
