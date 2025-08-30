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

// Renderizar tabla
function renderTabla() {
  const tbody = document.querySelector("#tablaColecciones tbody");
  tbody.innerHTML = "";

  colecciones.forEach((c, i) => {
    const row = `
      <tr>
        <td>${c.titulo}</td>
        <td>${c.descripcion}</td>
        <td>${c.hechos}</td>
        <td>${c.fuentes}</td>
        <td>${c.tag}</td>
        <td>
          <button class="btn btn-sm btn-warning me-1" onclick="editarColeccion(${i})">
            <i class="bi bi-pencil"></i>
          </button>
          <button class="btn btn-sm btn-danger" onclick="eliminarColeccion(${i})">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      </tr>
    `;
    tbody.insertAdjacentHTML("beforeend", row);
  });
}

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
