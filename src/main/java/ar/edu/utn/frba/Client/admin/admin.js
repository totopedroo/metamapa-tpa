document.addEventListener("DOMContentLoaded", () => {
  // -------------------------------
  // Modal Configurar Consenso
  // -------------------------------
  const modalConsenso = new bootstrap.Modal(document.getElementById("modalConsenso"));
  document.getElementById("configurarConsenso").addEventListener("click", () => {
    const form = document.getElementById("formConsenso");
    form.reset();
    form.classList.remove("was-validated");
    modalConsenso.show();
  });

  // Datos hardcodeados
  const colecciones = ["Colección A", "Colección B", "Colección C"];
  const algoritmos = ["Algoritmo 1", "Algoritmo 2", "Algoritmo 3"];

  const selectColeccion = document.getElementById("selectColeccion");
  const selectAlgoritmo = document.getElementById("selectAlgoritmo");

  // Llenar selects de consenso
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

  // Botón aceptar consenso
  const btnAceptarConsenso = document.getElementById("btnAceptarConsenso");
  btnAceptarConsenso.addEventListener("click", () => {
    const form = document.getElementById("formConsenso");
    if (!form.checkValidity()) {
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

  // -------------------------------
  // Modal Configurar Fuente
  // -------------------------------
  const modalFuente = new bootstrap.Modal(document.getElementById("modalFuente"));
  document.getElementById("configurarFuente").addEventListener("click", () => {
    const form = document.getElementById("formFuente");
    form.reset();
    form.classList.remove("was-validated");
    modalFuente.show();
  });

  const selectColeccionFuente = document.getElementById("selectColeccionFuente");

  // Llenar select de colecciones en modal fuente
  colecciones.forEach(c => {
    const option = document.createElement("option");
    option.value = c;
    option.textContent = c;
    selectColeccionFuente.appendChild(option);
  });

  // Botón aceptar fuente
  const btnAceptarFuente = document.getElementById("btnAceptarFuente");
  btnAceptarFuente.addEventListener("click", () => {
    const form = document.getElementById("formFuente");
    if (!form.checkValidity()) {
      form.classList.add("was-validated");
      return;
    }

    // Mostrar badge de confirmación
    const badge = document.getElementById("badgeConfirm");
    badge.classList.add("show");
    setTimeout(() => badge.classList.remove("show"), 2000);

    // Cerrar modal
    modalFuente.hide();

    // Reset form para la próxima vez
    form.reset();
    form.classList.remove("was-validated");
  });

  // -------------------------------
  // Modal Importar CSV
  // -------------------------------
  const modalCSV = new bootstrap.Modal(document.getElementById("modalCSV"));
  const formCSV = document.getElementById("formCSV");
  const inputCSV = document.getElementById("inputCSV");
  const progressContainer = document.getElementById("csvProgressContainer");
  const progressBar = document.getElementById("csvProgressBar");
  const btnImportarCSV = document.getElementById("btnImportarCSV");
  const btnCancelarCSV = document.getElementById("btnCancelarCSV");

  // Abrir modal al hacer click en la card
  document.getElementById("importarCSV").addEventListener("click", () => {
    formCSV.reset();
    formCSV.classList.remove("was-validated");
    progressContainer.classList.add("d-none");
    progressBar.style.width = "0%";
    progressBar.textContent = "0%";
    modalCSV.show();
  });

  // Validar e iniciar carga simulada
  btnImportarCSV.addEventListener("click", () => {
    if (!formCSV.checkValidity() || !inputCSV.files[0]) {
      formCSV.classList.add("was-validated");
      return;
    }

    const file = inputCSV.files[0];
    if (!file.name.endsWith(".csv")) {
      inputCSV.setCustomValidity("Formato inválido");
      formCSV.classList.add("was-validated");
      return;
    } else {
      inputCSV.setCustomValidity("");
    }

    // Mostrar barra de progreso y deshabilitar botones
    progressContainer.classList.remove("d-none");
    btnImportarCSV.disabled = true;
    btnCancelarCSV.disabled = true;

    let progress = 0;
    const interval = setInterval(() => {
      progress += 25; // avanza cada segundo
      progressBar.style.width = progress + "%";
      progressBar.textContent = progress + "%";

      if (progress >= 100) {
        clearInterval(interval);

        // Simular fin de carga
        setTimeout(() => {
          // Mostrar badge de confirmación
          const badge = document.getElementById("badgeConfirm");
          badge.textContent = "¡Importación completada!";
          badge.classList.add("show");
          setTimeout(() => {
            badge.classList.remove("show");
            badge.textContent = "¡Guardado con éxito!"; // volver al texto default
          }, 2000);

          // Resetear modal
          btnImportarCSV.disabled = false;
          btnCancelarCSV.disabled = false;
          modalCSV.hide();
          formCSV.reset();
          formCSV.classList.remove("was-validated");
          progressContainer.classList.add("d-none");
        }, 500);
      }
    }, 1000); // total 4 segundos
  });
});

// -------------------------------
// Navegación a solicitudes
// -------------------------------
document.getElementById("solicitudesEliminacion").addEventListener("click", () => {
  window.location.href = "solicitud-eliminacion/solicitud-eliminacion.html";
});

// -------------------------------
// Navegación a colecciones
// -------------------------------
document.getElementById("administrarColecciones").addEventListener("click", () => {
  window.location.href = "../colecciones/colecciones.html";
});

// -------------------------------
// Navegación a hechos admin
// -------------------------------
document.getElementById("administrarHechos").addEventListener("click", () => {
  window.location.href = "hechos-admin/hechos-admin.html";
});