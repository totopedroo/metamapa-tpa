// Inicializar el mapa
var map = L.map('map').setView([-34.6037, -58.3816], 12); // Buenos Aires

// Capa base
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '© OpenStreetMap contributors'
}).addTo(map);

// Marcadores de ejemplo
L.marker([-34.6037, -58.3816]).addTo(map)
  .bindPopup("<b>Hecho 1</b><br>Detalles del hecho en Buenos Aires.");

L.marker([-34.62, -58.38]).addTo(map)
  .bindPopup("<b>Hecho 2</b><br>Otro hecho registrado aquí.");
