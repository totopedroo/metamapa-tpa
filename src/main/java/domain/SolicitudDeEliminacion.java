package domain;


public class SolicitudDeEliminacion {
    public String justificacion;
    public String estado;

    public SolicitudDeEliminacion(String justificacion) {
        if (justificacion.length() > 500) {
            this.justificacion = justificacion;
            this.estado = "Pendiente";
        } else
            throw new RuntimeException("La justificacion no cumple con la cantidad minima de caracteres");
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void aceptarSolicitud() {
        cambiarEstado("Aceptado");
    }

    public void rechazarSolicitud() {
        cambiarEstado("Rechazado");
    }
}