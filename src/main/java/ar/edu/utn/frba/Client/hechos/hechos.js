let modal = document.getElementById("modalMapa");
    let ubicacionInput = document.getElementById("ubicacion");
    let map, marker, coords;

    function abrirMapa(){
      modal.style.display = "flex";
      if(!map){
        map = L.map('map').setView([-34.6037, -58.3816], 10); // Buenos Aires default
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap'
        }).addTo(map);

        map.on('click', function(e){
          if(marker) map.removeLayer(marker);
          marker = L.marker(e.latlng).addTo(map);
          coords = e.latlng;
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