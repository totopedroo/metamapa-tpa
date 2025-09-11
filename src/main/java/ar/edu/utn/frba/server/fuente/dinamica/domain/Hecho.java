package ar.edu.utn.frba.server.fuente.dinamica.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hecho {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    @Builder.Default private List<String> etiquetas = new ArrayList<>();
    @Builder.Default private List<SolicitudEliminacion> solicitudes = new ArrayList<>();
    private Contribuyente contribuyente;
    private boolean eliminado = false;

    public void agregarSolicitud(SolicitudEliminacion solicitud) {
        solicitudes.add(solicitud);
    }

    public void verificarEliminacion() {
        boolean tieneAceptada = solicitudes.stream().anyMatch(s -> s.getEstado().equals("Aceptado"));
        this.eliminado = tieneAceptada;
    }

    public Optional<ContenidoMultimedia> getContenidoMultimediaOpt() {
        return Optional.ofNullable(contenidoMultimedia);
    }

    public List<String> getEtiquetas() {
        return Collections.unmodifiableList(etiquetas);
    }

    public void marcarComoEliminado() {
        this.eliminado = true;
    }

    public boolean estaEliminado() {
        return eliminado;
    }

    public void agregarEtiqueta(String etiqueta) {
        this.etiquetas.add(etiqueta);
    }
}