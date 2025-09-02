let mapEditar, markerEditar;
let mapResultado, markerResultado;

document.addEventListener("DOMContentLoaded", () => {
  const hechos = [
    {
      titulo: "Foco activo en cordón serrano",
      descripcion: "Columna de humo visible desde ruta provincial.",
      tags: "Incendio forestal - Córdoba",
      estado: "Pendiente",
      lat: -31.4, lng: -64.2
    },
    {
      titulo: "Derrame en arroyo local",
      descripcion: "Vecinos reportan manchas y olor fuerte.",
      tags: "Contaminación - Neuquén",
      estado: "Pendiente",
      lat: -38.95, lng: -68.06
    },
    {
      titulo: "Vertido industrial no autorizado",
      descripcion: "Registro repetido por dos fuentes.",
      tags: "Contaminación - Chubut",
      estado: "Pendiente",
      lat: -43.3, lng: -65.1
    }
  ];

  const tbody = document.querySelector("#tablaHechos tbody");
  const badge = document.getElementById("badgeConfirm");

  function renderTabla(){
    tbody.innerHTML = "";
    hechos.forEach((h, index) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${h.titulo}</td>
        <td>${h.descripcion}</td>
        <td>${h.tags}</td>
        <td><button class="btn btn-sm btn-outline-primary ver-mapa" data-index="${index}">
          <i class="bi bi-geo-alt"></i> Ver
        </button></td>
        <td><span class="chip ${h.estado.toLowerCase()}">${h.estado}</span></td>
        <td>
          <button class="btn btn-sm btn-aceptar me-1" data-index="${index}">Aceptar</button>
          <button class="btn btn-sm btn-modificar me-1" data-index="${index}">Aceptar c/Modif.</button>
          <button class="btn btn-sm btn-rechazar" data-index="${index}">Rechazar</button>
        </td>
      `;
      tbody.appendChild(tr);
    });
  }

  renderTabla();

  // Manejo clicks en tabla
  tbody.addEventListener("click", (e) => {
    const index = e.target.dataset.index;
    if(index === undefined) return;

    if(e.target.classList.contains("btn-aceptar")){
      hechos[index].estado = "Aceptado";
      renderTabla();
      showBadge();
    } else if(e.target.classList.contains("btn-rechazar")){
      hechos[index].estado = "Rechazado";
      renderTabla();
      showBadge();
    } else if(e.target.classList.contains("btn-modificar")){
      abrirModalEditar(index);
    } else if(e.target.classList.contains("ver-mapa")){
      abrirMapaResultado(hechos[index].lat, hechos[index].lng);
    }
  });

  // --- Modal Editar ---
  function abrirModalEditar(index){
    const h = hechos[index];
    document.getElementById("editTitulo").value = h.titulo;
    document.getElementById("editDescripcion").value = h.descripcion;
    document.getElementById("editTags").value = h.tags;
    document.getElementById("editIndex").value = index;

    const modal = new bootstrap.Modal(document.getElementById("modalEditarHecho"));
    modal.show();

    setTimeout(() => {
      if(!mapEditar){
        mapEditar = L.map("map-editar").setView([h.lat, h.lng], 10);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
          attribution:'© OpenStreetMap'
        }).addTo(mapEditar);

        markerEditar = L.marker([h.lat, h.lng], {draggable:true}).addTo(mapEditar);
        markerEditar.on("dragend", (ev) => {
          const pos = ev.target.getLatLng();
          hechos[index].lat = pos.lat;
          hechos[index].lng = pos.lng;
        });
      } else {
        mapEditar.setView([h.lat, h.lng], 10);
        if(markerEditar) markerEditar.setLatLng([h.lat, h.lng]);
        mapEditar.invalidateSize();
      }
    }, 200);
  }

  // Guardar cambios
  document.getElementById("formEditarHecho").addEventListener("submit", (e) => {
    e.preventDefault();
    const index = document.getElementById("editIndex").value;
    hechos[index].titulo = document.getElementById("editTitulo").value;
    hechos[index].descripcion = document.getElementById("editDescripcion").value;
    hechos[index].tags = document.getElementById("editTags").value;
    hechos[index].estado = "Modificado";

    renderTabla();
    showBadge();

    bootstrap.Modal.getInstance(document.getElementById("modalEditarHecho")).hide();
  });

  // --- Modal Ver Mapa ---
  function abrirMapaResultado(lat, lng){
    const modal = new bootstrap.Modal(document.getElementById("modalMapaResultado"));
    modal.show();

    setTimeout(() => {
      if(!mapResultado){
        mapResultado = L.map('map-resultado').setView([lat,lng], 10);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',{
          attribution:'© OpenStreetMap'
        }).addTo(mapResultado);
        markerResultado = L.marker([lat,lng]).addTo(mapResultado);
      } else {
        mapResultado.setView([lat,lng], 10);
        if(markerResultado) mapResultado.removeLayer(markerResultado);
        markerResultado = L.marker([lat,lng]).addTo(mapResultado);
        mapResultado.invalidateSize();
      }
    }, 200);
  }

  function showBadge(){
    badge.classList.add("show");
    setTimeout(() => badge.classList.remove("show"), 2000);
  }
});
