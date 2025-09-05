function entrarUsuario(event) {
  event.preventDefault(); // evita que se recargue la página por el submit

  const usuario = document.getElementById("usuario").value.trim();

  if (usuario.toLowerCase() === "admin") {
    localStorage.setItem("rol", "admin");
    window.location.href = "../admin/admin.html";
  } else {
    localStorage.setItem("rol", "usuario");
    window.location.href = "../colecciones/colecciones.html";
  }

  
}

function entrarInvitado() {
  localStorage.setItem("rol", "invitado");
  window.location.href = "../colecciones/colecciones.html";
}
