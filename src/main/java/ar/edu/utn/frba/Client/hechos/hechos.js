document.addEventListener("DOMContentLoaded", () => {
  const rol = localStorage.getItem("rol");

  const logoutBtn = document.querySelector(".logout-btn");
  const invitadosBtn = document.querySelector(".btn-invitado-header");
  const hechosSubidos = document.querySelector("#subidos"); 
  const btnAgregar = document.querySelector(".title-section button");

  if (rol === "invitado") {
    if (logoutBtn) logoutBtn.style.display = "none";
    if (hechosSubidos) hechosSubidos.style.display = "none";
    if (btnAgregar) btnAgregar.style.display = "none";
    if (invitadosBtn) invitadosBtn.style.display = "inline-block"; // mostrar botón
  } else {
    // usuario normal
    if (invitadosBtn) invitadosBtn.style.display = "none";
  }
});


function cerrarSesion() {
  localStorage.removeItem("rol");
}

let modal = document.getElementById("modalMapa");
    let ubicacionInput = document.getElementById("ubicacion");
    let map, marker, coords, circle;

    function abrirMapa(){
      modal.style.display = "flex";

      if(!map){
        map = L.map('map').setView([-34.6037, -58.3816], 10); // Buenos Aires default

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap'
        }).addTo(map);

        map.on('click', function(e){
          // Eliminar marcador anterior
          if(marker) map.removeLayer(marker);
          // Eliminar círculo anterior
          if(circle) map.removeLayer(circle);

          // Agregar nuevo marcador
          marker = L.marker(e.latlng).addTo(map);
          coords = e.latlng;

          // Agregar círculo de 500 metros (por ejemplo)
          circle = L.circle(e.latlng, {
            color: 'blue',
            fillColor: '#blue',
            fillOpacity: 0.2,
            radius: 10000 // radio en metros
          }).addTo(map);
        });
      }
    }

    function cerrarMapa(){
      modal.style.display = "none";
      if(coords){
        ubicacionInput.value = `Lat: ${coords.lat.toFixed(4)}, Lng: ${coords.lng.toFixed(4)}`;
      }
    }

    function buscar(){
      document.getElementById("resultados").style.display = "block";
    }

    function limpiar(){
      document.getElementById("fecha-desde").value = "";
      document.getElementById("fecha-hasta").value = "";
      ubicacionInput.value = "";
      document.querySelectorAll("#categoria-options input").forEach(c => c.checked = false);
      document.getElementById("curada").checked = false;
      document.getElementById("irrestricta").checked = false;
      document.getElementById("resultados").style.display = "none";
    }

    // Multi-select categorías
    function toggleCategorias(){
      document.getElementById("categoria-options").style.display =
        document.getElementById("categoria-options").style.display === "block" ? "none" : "block";
    }

    window.onclick = function(e){
      if(e.target == modal){ modal.style.display = "none"; }
      if(!e.target.closest(".multi-select")){
        document.getElementById("categoria-options").style.display = "none";
      }
    }

    let modalHecho = document.getElementById("modalHecho");
let ubicacionHechoInput = document.getElementById("ubicacion-hecho");
let mapHecho, markerHecho, coordsHecho;

// Abrir modal Agregar Hecho
document.querySelector(".title-section .btn-primary").addEventListener("click", () => {
  modalHecho.style.display = "flex";
});

// Cerrar modal
function cerrarHecho(){
  modalHecho.style.display = "none";
  document.getElementById("formHecho").reset();
  document.getElementById("map-hecho").style.display = "none";
  if(mapHecho){ mapHecho.remove(); mapHecho = null; }
}

// Multi-select categorías (formulario)
function toggleCategoriasForm(){
  let opciones = document.getElementById("categoria-form-options");
  opciones.style.display = (opciones.style.display === "block" ? "none" : "block");
}

// Ubicación con Leaflet en formulario
function toggleMapaHecho(){
  const mapaDiv = document.getElementById("map-hecho");
  const flecha = document.querySelector(".ubicacion-wrapper .toggle-map");

  if(mapaDiv.style.display === "block"){
    mapaDiv.style.display = "none";
    flecha.classList.remove("open"); // rotar hacia abajo
  } else {
    mapaDiv.style.display = "block";
    flecha.classList.add("open"); // rotar hacia arriba
    abrirMapaHecho();
  }
}

function abrirMapaHecho(){
  if(!mapHecho){
    mapHecho = L.map('map-hecho', {scrollWheelZoom:true}).setView([-34.6037, -58.3816], 10);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(mapHecho);

    mapHecho.on('click', function(e){
      if(markerHecho) mapHecho.removeLayer(markerHecho);
      markerHecho = L.marker(e.latlng).addTo(mapHecho);
      coordsHecho = e.latlng;
      ubicacionHechoInput.value = `Lat: ${coordsHecho.lat.toFixed(4)}, Lng: ${coordsHecho.lng.toFixed(4)}`;
    });
  } else {
    mapHecho.invalidateSize(); // importante para que Leaflet se dibuje correctamente
  }
}

// Guardar formulario (demo: muestra en consola)
document.getElementById("formHecho").addEventListener("submit", (e) => {
  e.preventDefault();
  let titulo = document.getElementById("titulo-hecho").value;
  let descripcion = document.getElementById("descripcion-hecho").value;
  let fecha = document.getElementById("fecha-hecho").value;
  let media = document.getElementById("media-hecho").files;
  
  let categorias = [];
  document.querySelectorAll("#categoria-form-options input:checked").forEach(c => categorias.push(c.value));

  console.log({titulo, descripcion, categorias, coordsHecho, fecha, media});
  alert("Hecho guardado (ver consola)");

  cerrarHecho();
});

// Cerrar modal al click fuera
window.onclick = function(e){
  if(e.target == modal){ modal.style.display = "none"; }
  if(e.target == modalHecho){ cerrarHecho(); }
  if(!e.target.closest(".multi-select")){
    document.getElementById("categoria-options").style.display = "none";
    document.getElementById("categoria-form-options").style.display = "none";
  }
}


let mapResultado, markerResultado;

function abrirMapaResultado(lat,lng){
  const modal = new bootstrap.Modal(document.getElementById('modalMapaResultado'));
  modal.show();

  setTimeout(() => { // espera a que el modal se renderice
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

function abrirModalEliminacion(titulo){
  document.getElementById('hecho-eliminar').innerText = titulo;
  document.getElementById('justificacion-eliminacion').value = '';
  const modal = new bootstrap.Modal(document.getElementById('modalEliminacion'));
  modal.show();
}

function enviarEliminacion(){
  const justificacion = document.getElementById('justificacion-eliminacion').value;
  if(justificacion.length < 500){
    alert('La justificación debe tener al menos 500 caracteres');
    return;
  }
  alert('Solicitud enviada!');
  bootstrap.Modal.getInstance(document.getElementById('modalEliminacion')).hide();
}
