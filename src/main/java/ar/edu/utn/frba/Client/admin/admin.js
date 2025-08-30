document.addEventListener("DOMContentLoaded", () => {
  // Abrir modal al hacer click en la card
  const modalConsenso = new bootstrap.Modal(document.getElementById("modalConsenso"));
  document.getElementById("configurarConsenso").addEventListener("click", () => {
    modalConsenso.show();
  });

  // Datos hardcodeados
  const colecciones = ["Colección A", "Colección B", "Colección C"];
  const algoritmos = ["Algoritmo 1", "Algoritmo 2", "Algoritmo 3"];

  const selectColeccion = document.getElementById("selectColeccion");
  const selectAlgoritmo = document.getElementById("selectAlgoritmo");

  // Llenar selects
  colecciones.forEach(c => {
    const option = document.createElement("option");
    option.value = c;
    option.textContent = c;
    selectColeccion.appendChild(option);
  });

  algoritmos.forEach(a => {
    const option = document.createElement("option");
    option.value = a;
    option.textContent = a;
    selectAlgoritmo.appendChild(option);
  });

  // Botón aceptar
  const btnAceptar = document.getElementById("btnAceptarConsenso");
  btnAceptar.addEventListener("click", () => {
    const form = document.getElementById("formConsenso");
    if(!form.checkValidity()){
      form.classList.add("was-validated");
      return;
    }

    // Mostrar badge de confirmación
    const badge = document.getElementById("badgeConfirm");
    badge.classList.add("show");
    setTimeout(() => badge.classList.remove("show"), 2000);

    // Cerrar modal
    modalConsenso.hide();

    // Reset form para la próxima vez
    form.reset();
    form.classList.remove("was-validated");
  });
});

// Abrir página de solicitudes al hacer click en la card
document.getElementById("solicitudesEliminacion").addEventListener("click", () => {
  window.location.href = "solicitud-eliminacion/solicitud-eliminacion.html";
});
