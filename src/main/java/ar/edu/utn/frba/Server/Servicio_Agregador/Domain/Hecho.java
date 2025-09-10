package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private Optional<Boolean> consensuado = Optional.of(false);

    private TipoFuente fuente;
    private String provincia;  // Nombre legible de la provincia
    private Integer hora;

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
    public Hecho(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia,
                 Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, long idHecho, TipoFuente fuente, String provincia, Integer hora) {
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
        this.provincia = provincia;
        this.hora = hora;

    }

    public Hecho() {

    }

    public void agregarSolicitud(SolicitudEliminacion solicitud) {
        solicitudes.add(solicitud);
    }

    public void verificarEliminacion() {
        boolean tieneAceptada = solicitudes.stream().anyMatch(s -> s.getEstado().equals("Aceptado"));
        this.eliminado = tieneAceptada;
    }
    public boolean getEliminado() {return eliminado;}
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

    public Optional<ContenidoMultimedia> getContenidoMultimedia() {
        return contenidoMultimedia == null ? Optional.empty() : contenidoMultimedia;
    }

    // setter null-safe
    public void setContenidoMultimedia(Optional<ContenidoMultimedia> contenidoMultimedia) {
        this.contenidoMultimedia = (contenidoMultimedia == null) ? Optional.empty() : contenidoMultimedia;
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

    public long getId(){
        return idHecho;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hecho hecho = (Hecho) o;
        return Objects.equals(idHecho, hecho.idHecho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHecho);
    }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public Integer getHora() { return hora; }
    public void setHora(Integer hora) {
        if (hora == null) { this.hora = null; return; }
        // clamp 0..23
        this.hora = Math.max(0, Math.min(23, hora));
    }


    public LocalDateTime getFechaHora() {
        if (fechaAcontecimiento == null) return null;
        int h = (hora == null) ? 0 : Math.max(0, Math.min(23, hora));
        return fechaAcontecimiento.atTime(h, 0);
    }


    public boolean isEliminado() {
        return eliminado;
    }
}
