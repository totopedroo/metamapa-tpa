package mocks;

public class SolicitudEliminacion {

    private String motivo;
    private EstadoSolicitud estado;

    public enum EstadoSolicitud {
        PENDIENTE, ACEPTADA, RECHAZADA
    }

    public SolicitudEliminacion(String motivo) {
        this.motivo = motivo;
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public void aceptar() {
        this.estado = EstadoSolicitud.ACEPTADA;
        System.out.println("Solicitud aceptada.");
    }

    public void rechazar() {
        this.estado = EstadoSolicitud.RECHAZADA;
        System.out.println("Solicitud rechazada.");
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }
}
