package ar.edu.utn.frba.Fuente_Proxy.Domain;


import ar.edu.utn.frba.Enums.EstadoDeSolicitud;
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
        if (justificacion.length() < 500) {
            throw new RuntimeException("La justificación no cumple con la cantidad mínima de caracteres");
        }
        this.justificacion = justificacion;
        this.estado = EstadoDeSolicitud.PENDIENTE;
        this.idHechoAsociado = idHechoAsociado;
    }

    public void aceptarSolicitud() {
        estado = EstadoDeSolicitud.ACEPTADA;
    }

    public void rechazarSolicitud() {
        estado =EstadoDeSolicitud.RECHAZADA;
    }

    public String getJustificacion() { return justificacion; }
    public EstadoDeSolicitud getEstado() { return estado; }
    public long getIdSolicitud() { return idSolicitud; }
    public long getIdHechoAsociado() { return idHechoAsociado; }

    public void setId(long idSolicitud) { this.idSolicitud = idSolicitud; }
}