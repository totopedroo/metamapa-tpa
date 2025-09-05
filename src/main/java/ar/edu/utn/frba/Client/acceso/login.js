function entrarUsuario() {
  localStorage.setItem("rol", "usuario");
  window.location.href = "../hechos/hechos.html";
}

function entrarInvitado() {
  localStorage.setItem("rol", "invitado");
  window.location.href = "../hechos/hechos.html";
}
