package ar.edu.utn.frba.server.servicioAgregador.domain;


import ar.edu.utn.frba.server.common.enums.EstadoDeSolicitud;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSolicitud;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificacion;
    @Column
    @Enumerated(EnumType.STRING)
    private EstadoDeSolicitud estado;
    @Column(name = "id_hecho_asociado")
    @JoinColumn
    private long idHechoAsociado;
    @Column(name = "es_spam")
    private boolean esSpam = false;

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
