package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHecho;
    @Column(name = "titulo", columnDefinition = "varchar(255)")
    private String titulo;
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    @Column(name = "categoria", columnDefinition = "varchar(100)")
    private String categoria;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contenido_multimedia_id", referencedColumnName = "id", nullable = true)
    private ContenidoMultimedia contenidoMultimedia;
    @Column(name = "latitud")
    private Double latitud;
    @Column(name = "longitud")
    private Double longitud;
    @Column(name = "fecha_acontecimiento")
    private LocalDate fechaAcontecimiento;
    @Column(name = "fecha_carga")
    private LocalDate fechaCarga;
    @Column(name = "provincia", columnDefinition = "varchar(100)")
    private String provincia;
    @Column(name = "hora_acontecimiento")
    private LocalTime horaAcontecimiento;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hecho_etiquetado", joinColumns = @JoinColumn(name = "hecho_id"), inverseJoinColumns = @JoinColumn(name = "etiqueta_id"))
    private List<Etiqueta> etiquetas = new ArrayList<>();
    @OneToMany(mappedBy = "idHechoAsociado")
    private List<SolicitudEliminacion> solicitudes = new ArrayList<>();
    @Transient
    private Contribuyente contribuyente;
    @Column(name = "eliminado")
    private boolean eliminado = false;
    @Column(name = "consensuado", nullable = true)
    private Boolean consensuado = false;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hecho_fuente", joinColumns = @JoinColumn(name = "hechos_id"), inverseJoinColumns = @JoinColumn(name = "fuente_id"))
    private List<Fuente> fuente = new ArrayList<>();
    @ManyToMany(mappedBy = "hechos")
    private List<Coleccion> colecciones = new ArrayList<>();

    public Hecho(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia,
            Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga,
            String provincia, LocalTime horaAcontecimiento, long idHecho, Fuente fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.contenidoMultimedia = contenidoMultimedia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.provincia = provincia;
        this.horaAcontecimiento = horaAcontecimiento;
        this.idHecho = idHecho;
        this.fuente.add(fuente);
    }

    public void agregarFuente (Fuente fuente) {
        this.fuente.add(fuente);
    }

    public void agregarColeccion (Coleccion coleccion) {
        this.colecciones.add(coleccion);
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

    public void agregarEtiqueta(Etiqueta etiqueta) {
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
        return contenidoMultimedia;
    }

    // setter null-safe
    public void setContenidoMultimedia(ContenidoMultimedia contenidoMultimedia) {
        this.contenidoMultimedia = contenidoMultimedia;
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public LocalTime getHoraAcontecimiento() {
        return horaAcontecimiento;
    }

    public void setHoraAcontecimiento(LocalTime horaAcontecimiento) {
        this.horaAcontecimiento = horaAcontecimiento;
    }

    public Long getIdHecho() {
        return idHecho;
    }

    public void setIdHecho(Long idHecho) {
        this.idHecho = idHecho;
    }

    public List<Fuente> getTipoFuente() {
        return fuente;
    }

    public void setTipoFuente(Fuente fuente) {
        this.fuente.add(fuente);
    }

    public boolean esIgualA(Hecho otro) {
        return this.titulo.equalsIgnoreCase(otro.titulo)
                && Objects.equals(this.descripcion, otro.descripcion)
                && this.latitud == otro.latitud
                && this.longitud == otro.longitud
                && Objects.equals(this.categoria, otro.categoria)
                && Objects.equals(this.fechaAcontecimiento, otro.fechaAcontecimiento)
                && Objects.equals(this.provincia, otro.provincia)
                && Objects.equals(this.horaAcontecimiento, otro.horaAcontecimiento);
    }

    public long getId() {
        return idHecho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Hecho hecho = (Hecho) o;
        return Objects.equals(idHecho, hecho.idHecho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHecho);
    }
}
