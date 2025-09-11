package ar.edu.utn.frba.server.servicioAgregador.domain;


import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudEliminacion {
    private String justificacion;
    private EstadoDeSolicitud estado;
    private long idSolicitud;
    private long idHechoAsociado;

    public SolicitudEliminacion(String justificacion, long idHechoAsociado) {
        if (justificacion == null || justificacion.trim().length() < 500) {
            throw new RuntimeException("La justificación no cumple con la cantidad mínima de caracteres");
        }
        this.justificacion = justificacion.trim();
        this.estado = EstadoDeSolicitud.PENDIENTE;
        this.idHechoAsociado = idHechoAsociado;
    }

    public void aceptarSolicitud() { this.estado = EstadoDeSolicitud.ACEPTADA; }
    public void rechazarSolicitud() { this.estado = EstadoDeSolicitud.RECHAZADA; }
    public void rechazarPorSpam() { this.estado = EstadoDeSolicitud.RECHAZADA; } // alias explícito
}
