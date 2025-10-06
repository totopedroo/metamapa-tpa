package ar.edu.utn.frba.server.servicioAgregador.domain;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import lombok.*;

import java.time.LocalDate;
import java.util.*;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Builder
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
    @ManyToMany
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
    @ManyToMany
    @JoinTable(name = "hecho_fuente", joinColumns = @JoinColumn(name = "hechos"), inverseJoinColumns = @JoinColumn(name = "id"))
    private List<Fuente> fuente = new ArrayList<>();
    @ManyToMany(mappedBy = "hechos")
    private List<Coleccion> colecciones;
    // NUEVO: estado del consenso para navegación curada
    @Builder.Default
    private EstadoConsenso estadoConsenso = EstadoConsenso.CONSENSUADO;



    // === utilidades ===
    public Optional<ContenidoMultimedia> getContenidoMultimediaOpt() {
        return Optional.ofNullable(contenidoMultimedia);
    }

    public List<Etiqueta> getEtiquetas() { return Collections.unmodifiableList(etiquetas); }

    public void agregarEtiqueta(Etiqueta etiqueta) { this.etiquetas.add(etiqueta); }

    public void agregarSolicitud(SolicitudEliminacion solicitud) { solicitudes.add(solicitud); }


    // Enum-based (sin strings mágicos)
    public void verificarEliminacion() {
        boolean tieneAceptada = solicitudes.stream()
                .anyMatch(s -> s.getEstado() == EstadoDeSolicitud.ACEPTADA);
        this.eliminado = tieneAceptada;
    }

    public void marcarComoEliminado() { this.eliminado = true; }

    public boolean estaEliminado() { return eliminado; }


    // Navegación curada
    public boolean isConsensuado() { return estadoConsenso == EstadoConsenso.CONSENSUADO; }

    public void setEstadoConsenso(EstadoConsenso e) {
        this.estadoConsenso = (e == null ? EstadoConsenso.CONSENSUADO : e);
    }


    // Igualdad de consenso (null-safe + tolerancia espacial)
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
        return Math.abs(a - b) <= 0.001; // ~111m
    }

    public void setFuente(Fuente fuente) {
        this.fuente = (List<Fuente>) fuente;
    }

    // identidad por id
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hecho h)) return false;
        return Objects.equals(idHecho, h.idHecho);
    }

    @Override public int hashCode() { return Objects.hash(idHecho); }
}