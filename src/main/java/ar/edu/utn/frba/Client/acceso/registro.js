document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("register-form");
  const errorMsg = document.getElementById("error-msg");
  const modal = document.getElementById("success-modal");
  const closeModal = document.getElementById("close-modal");

  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const nombre = document.getElementById("nombre").value.trim();

    if (!nombre) {
      errorMsg.textContent = "⚠️ El nombre es obligatorio.";
      return;
    }

    errorMsg.textContent = "";

    // Mostrar modal de éxito
    modal.classList.add("show");
  });

  closeModal.addEventListener("click", () => {
    modal.classList.remove("show");
    form.reset();
  });
});
