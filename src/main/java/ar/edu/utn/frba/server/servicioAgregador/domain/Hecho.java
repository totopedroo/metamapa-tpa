package ar.edu.utn.frba.server.servicioAgregador.domain;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

import jakarta.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hecho")
@Builder
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hecho")
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

    @ManyToMany
    @JoinTable(name = "hecho_etiquetado", joinColumns = @JoinColumn(name = "hecho_id"), inverseJoinColumns = @JoinColumn(name = "etiqueta_id"))
    private List<Etiqueta> etiquetas = new ArrayList<>();

    @OneToMany(mappedBy = "idHechoAsociado")
    private List<SolicitudEliminacion> solicitudes = new ArrayList<>();

    @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitudModificacion> solicitudesModificacion = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contribuyente_id")
    private Usuario contribuyente;

    @Column(name = "eliminado")
    private boolean eliminado = false;

    @Column(name = "consensuado", nullable = true)
    private Boolean consensuado = false;

    // --- CORRECCIÓN 1: Agregamos CascadeType.ALL para que guarde la fuente automáticamente ---
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "hecho_fuente",
            joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id_hecho"),
            inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
    )
    private List<Fuente> fuente = new ArrayList<>(); // Nota: Sería mejor llamarlo "fuentes" (plural), pero lo dejo así para no romper tu código.

    @ManyToMany(mappedBy = "hechos")
    private List<Coleccion> colecciones;

    @Builder.Default
    private EstadoConsenso estadoConsenso = EstadoConsenso.CONSENSUADO;

    // Constructor auxiliar que tenías
    public Hecho(String titulo, String descripcion, String categoria, Object o, Double latitud, Double longitud, OffsetDateTime fechaHecho, LocalDate now, Long id) {
    }

    // === utilidades ===
    public Optional<ContenidoMultimedia> getContenidoMultimediaOpt() {
        return Optional.ofNullable(contenidoMultimedia);
    }

    public List<Etiqueta> getEtiquetas() { return Collections.unmodifiableList(etiquetas); }

    public void agregarEtiqueta(Etiqueta etiqueta) { this.etiquetas.add(etiqueta); }

    public void agregarSolicitud(SolicitudEliminacion solicitud) { solicitudes.add(solicitud); }

    public void verificarEliminacion() {
        boolean tieneAceptada = solicitudes.stream()
                .anyMatch(s -> s.getEstado() == EstadoDeSolicitud.ACEPTADA);
        this.eliminado = tieneAceptada;
    }

    public void marcarComoEliminado() { this.eliminado = true; }

    public boolean estaEliminado() { return eliminado; }

    public boolean isConsensuado() { return estadoConsenso == EstadoConsenso.CONSENSUADO; }

    public void setEstadoConsenso(EstadoConsenso e) {
        this.estadoConsenso = (e == null ? EstadoConsenso.CONSENSUADO : e);
    }

    public boolean esIgualA(Hecho otro) {
        if (otro == null) return false;
        boolean tit = Objects.equals(normalizar(this.titulo), normalizar(otro.titulo));
        boolean des = Objects.equals(this.descripcion, otro.descripcion);
        boolean cat = Objects.equals(this.categoria, otro.categoria);
        boolean fec = Objects.equals(this.fechaAcontecimiento, otro.fechaAcontecimiento);
        boolean latOk = cmp(this.latitud, otro.latitud);
        boolean lonOk = cmp(this.longitud, otro.longitud);
        return tit && des && cat && fec && latOk && lonOk;
    }

    public String claveConsenso() {
        String t  = normalizar(titulo);
        String f  = fechaAcontecimiento == null ? "" : fechaAcontecimiento.toString();
        String la = latitud  == null ? "" : String.format(Locale.ROOT, "%.3f", latitud);
        String lo = longitud == null ? "" : String.format(Locale.ROOT, "%.3f", longitud);
        return String.join("|", t, f, la, lo);
    }

    private static String normalizar(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private static boolean cmp(Double a, Double b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return Math.abs(a - b) <= 0.001;
    }

    // --- CORRECCIÓN 2: El Setter arreglado ---
    // Como 'fuente' es una LISTA, no puedes castear un objeto solo.
    // Lo que hacemos es limpiar la lista y agregar el nuevo elemento.
    public void setFuente(Fuente fuenteUnica) {
        if (this.fuente == null) {
            this.fuente = new ArrayList<>();
        } else {
            this.fuente.clear(); // Reemplazamos las fuentes anteriores
        }

        if (fuenteUnica != null) {
            this.fuente.add(fuenteUnica);
        }
    }

    // Método opcional si quieres agregar sin borrar las anteriores
    public void agregarFuente(Fuente f) {
        if (this.fuente == null) this.fuente = new ArrayList<>();
        this.fuente.add(f);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hecho h)) return false;
        return Objects.equals(idHecho, h.idHecho);
    }

    @Override public int hashCode() { return Objects.hash(idHecho); }

    public void agregarSolicitudModificacion(SolicitudModificacion solicitud) {
        solicitudesModificacion.add(solicitud);
        solicitud.setHecho(this); // Vinculación bidireccional
    }

    // Método para remover
    public void removerSolicitudModificacion(SolicitudModificacion solicitud) {
        solicitudesModificacion.remove(solicitud);
        solicitud.setHecho(this);
    }
}