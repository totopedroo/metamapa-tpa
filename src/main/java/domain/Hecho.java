package domain;
import lombok.Getter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hecho {
    public String titulo;
    public String descripcion;
    public String categoria;
    public Optional<ContenidoMultimedia> contenidoMultimedia;
    public Double latitud;
    public Double longitud;
    public LocalDate fechaAcontecimiento;
    public LocalDate fechaCarga;
    public List<String> etiquetas = new ArrayList();
    public List <SolicitudDeEliminacion> solicitudes= new ArrayList();
    public boolean eliminado = false;

    public Hecho(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia,
                 Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.contenidoMultimedia = Optional.ofNullable(contenidoMultimedia);
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
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
}