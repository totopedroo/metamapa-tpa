package domain;


import Enums.EstadoDeSolicitud;

public class SolicitudDeEliminacion {
    public String justificacion;
    public EstadoDeSolicitud estado;

    public SolicitudDeEliminacion(String justificacion) {
        if (justificacion.length() > 500) {
            this.justificacion = justificacion;
            this.estado = EstadoDeSolicitud.PENDIENTE;
        } else
            throw new RuntimeException("La justificacion no cumple con la cantidad minima de caracteres");
    }



    public void aceptarSolicitud() {
        estado = EstadoDeSolicitud.ACEPTADA;
    }

    public void rechazarSolicitud() {
        estado =EstadoDeSolicitud.RECHAZADA;
    }
}