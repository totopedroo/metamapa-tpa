package ar.edu.utn.frba.server.servicioAgregador.domain;

import ar.edu.utn.frba.server.contratos.enums.CampoHecho;
import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud_modificacion")
public class SolicitudModificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CORRECCIÓN 1: Usamos el objeto para mantener la relación bidireccional
    // y que funcione la lista en la clase Hecho.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hecho", nullable = false)
    private Hecho hecho;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampoHecho campo;

    @Column(name = "valor_nuevo", columnDefinition = "TEXT")
    private String valorNuevo;

    // Aquí sí puedes dejar el ID suelto si no te interesa navegar al usuario
    // Pero cambiamos @JoinColumn por @Column
    @Column(name = "id_usuario")
    private Long idContribuyente;

    @Enumerated(EnumType.STRING)
    private EstadoDeSolicitud estado = EstadoDeSolicitud.PENDIENTE;

    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    private String justificacion;

    // CORRECCIÓN 2: Constructor completo y validación lógica
    public SolicitudModificacion(Hecho hecho, CampoHecho campo, String valorNuevo, String justificacion, Long idContribuyente) {
        // Validación ajustada: 500 chars era mucho para un cambio chico.
        if (justificacion != null && !justificacion.isBlank() && justificacion.length() > 1000) {
            throw new RuntimeException("La justificación es demasiado larga");
        }

        this.hecho = hecho;
        this.campo = campo;
        this.valorNuevo = valorNuevo;
        this.justificacion = justificacion;
        this.idContribuyente = idContribuyente;
        this.estado = EstadoDeSolicitud.PENDIENTE;
        this.fechaSolicitud = LocalDateTime.now();
    }



    public void aceptarSolicitud() { this.estado = EstadoDeSolicitud.ACEPTADA; }
    public void rechazarSolicitud() { this.estado = EstadoDeSolicitud.RECHAZADA; }
} // alias explícito

