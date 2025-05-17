package ar.edu.utn.frba.domain;
import lombok.Getter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Setter;

@Getter
@Setter
public class Hecho {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Optional<ContenidoMultimedia> contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private List<String> etiquetas = new ArrayList();
    private List <SolicitudDeEliminacion> solicitudes= new ArrayList();
    private boolean eliminado = false;

    public Hecho(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia,
                 Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, long idHecho) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.contenidoMultimedia = Optional.ofNullable(contenidoMultimedia);
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.idHecho = idHecho;
    }

    public void agregarSolicitud(SolicitudDeEliminacion solicitud) {
        solicitudes.add(solicitud);
    }

    public void verificarEliminacion() {
        boolean tieneAceptada = solicitudes.stream().anyMatch(s -> s.estado.equals("Aceptado"));
        this.eliminado = tieneAceptada;
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