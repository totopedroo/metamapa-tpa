package ar.edu.utn.frba.domain;


import ar.edu.utn.frba.Enums.EstadoDeSolicitud;

public class SolicitudDeEliminacion {
    public String justificacion;
    public EstadoDeSolicitud estado;
    public long idSolicitud;
    public long idHechoAsociado;

    public SolicitudDeEliminacion(String justificacion, long idSolicitud, long idHechoAsociado) {
        if (justificacion.length() > 500) {
            this.justificacion = justificacion;
            this.estado = EstadoDeSolicitud.PENDIENTE;
        } else
            throw new RuntimeException("La justificacion no cumple con la cantidad minima de caracteres");
        this.idSolicitud = idSolicitud;
        this.idHechoAsociado = idHechoAsociado;
    }



    public void aceptarSolicitud() {
        estado = EstadoDeSolicitud.ACEPTADA;
    }

    public void rechazarSolicitud() {
        estado =EstadoDeSolicitud.RECHAZADA;
    }
}