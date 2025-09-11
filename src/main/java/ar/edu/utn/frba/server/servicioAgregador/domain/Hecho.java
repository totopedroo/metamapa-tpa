package ar.edu.utn.frba.server.servicioAgregador.domain;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Setter @Getter
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
    private TipoFuente tipoFuente;

    // NUEVO: estado del consenso para navegación curada
    @Builder.Default
    private EstadoConsenso estadoConsenso = EstadoConsenso.CONSENSUADO;


    // === utilidades ===
    public Optional<ContenidoMultimedia> getContenidoMultimediaOpt() {
        return Optional.ofNullable(contenidoMultimedia);
    }

    public List<String> getEtiquetas() { return Collections.unmodifiableList(etiquetas); }

    public void agregarEtiqueta(String etiqueta) { this.etiquetas.add(etiqueta); }

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


    // identidad por id
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hecho h)) return false;
        return Objects.equals(idHecho, h.idHecho);
    }

    @Override public int hashCode() { return Objects.hash(idHecho); }
}