package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import ar.edu.utn.frba.Server.Enums.EstadoDeSolicitud;
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
        estado = EstadoDeSolicitud.RECHAZADA;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public EstadoDeSolicitud getEstado() {
        return estado;
    }

    public long getIdSolicitud() {
        return idSolicitud;
    }

    public long getIdHechoAsociado() {
        return idHechoAsociado;
    }

    public void setEstado(EstadoDeSolicitud estado) {
        this.estado = estado;
    }

    public void setId(long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public boolean isEsSpam() {
        return esSpam;
    }

    public void setEsSpam(boolean esSpam) {
        this.esSpam = esSpam;
    }
}
