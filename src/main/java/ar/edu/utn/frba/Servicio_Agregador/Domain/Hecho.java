package ar.edu.utn.frba.Servicio_Agregador.Domain;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Contribuyente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.ContenidoMultimedia;
import ar.edu.utn.frba.Servicio_Agregador.Domain.SolicitudEliminacion;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;

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
    private List<String> etiquetas = new ArrayList<>();
    private List<SolicitudEliminacion> solicitudes = new ArrayList<>();
    private Contribuyente contribuyente;
    private boolean eliminado = false;
    private Optional<Boolean> consensuado;
    private TipoFuente fuente;

    public Hecho(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia,
                 Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, long idHecho, TipoFuente fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.contenidoMultimedia = Optional.ofNullable(contenidoMultimedia);
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.idHecho = idHecho;
        this.fuente = fuente;

    }

    public void agregarSolicitud(SolicitudEliminacion solicitud) {
        solicitudes.add(solicitud);
    }

    public void verificarEliminacion() {
        boolean tieneAceptada = solicitudes.stream().anyMatch(s -> s.getEstado().equals("Aceptado"));
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ContenidoMultimedia getContenidoMultimedia() {
        return contenidoMultimedia.orElse(null);
    }

    public void setContenidoMultimedia(ContenidoMultimedia contenidoMultimedia) {
        this.contenidoMultimedia = Optional.ofNullable(contenidoMultimedia);
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public LocalDate getFechaAcontecimiento() {
        return fechaAcontecimiento;
    }

    public void setFechaAcontecimiento(LocalDate fechaAcontecimiento) {
        this.fechaAcontecimiento = fechaAcontecimiento;
    }

    public LocalDate getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDate fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public Long getIdHecho() {
        return idHecho;
    }

    public void setIdHecho(Long idHecho) {
        this.idHecho = idHecho;
    }

    public TipoFuente getTipoFuente() {
        return fuente;
    }

    public void setTipoFuente(TipoFuente fuente) {this.fuente = fuente;}

    public boolean esIgualA(Hecho otro) {
        return this.titulo.equalsIgnoreCase(otro.titulo)
                && Objects.equals(this.descripcion, otro.descripcion)
                && this.latitud == otro.latitud
                && this.longitud == otro.longitud
                && Objects.equals(this.categoria, otro.categoria)
                && Objects.equals(this.fechaAcontecimiento, otro.fechaAcontecimiento);
    }

}