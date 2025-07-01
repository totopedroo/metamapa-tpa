package ar.edu.utn.frba.Fuente_Dinamica.Dtos;

import ar.edu.utn.frba.Fuente_Dinamica.Domain.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
public class HechosOutputDto {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Optional<ContenidoMultimedia> contenidoMultimedia = Optional.empty();
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private List<String> etiquetas;
    private List<SolicitudEliminacion> solicitudes;
    private Contribuyente contribuyente;
    private boolean eliminado = false;
    private Fuente fuente;
    public HechosOutputDto() {
    }

    public HechosOutputDto(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia, Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, Contribuyente contribuyente, Object o, Long idHecho) {
    }

    public HechosOutputDto(Long idHecho, String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia, Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, List<String> etiquetas, List<SolicitudEliminacion> solicitudes, Contribuyente contribuyente) {
    }

    public HechosOutputDto(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia, Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, Contribuyente contribuyente, Long idHecho) {
    }

    public static HechosOutputDto fromModel(Hecho hecho){
        HechosOutputDto dto = new HechosOutputDto();
        dto.idHecho = hecho.getIdHecho();
        dto.titulo = hecho.getTitulo();
        dto.descripcion = hecho.getDescripcion();
        dto.categoria = hecho.getCategoria();
        dto.latitud = hecho.getLatitud();
        dto.longitud = hecho.getLongitud();
        dto.contribuyente = hecho.getContribuyente();
        dto.eliminado = hecho.isEliminado();
        return dto;
    }

    public HechosOutputDto(Long idHecho, String titulo, String descripcion, String categoria,
                           ContenidoMultimedia contenidoMultimedia, Double latitud, Double longitud,
                           LocalDate fechaAcontecimiento, LocalDate fechaCarga,
                           List<String> etiquetas, List<SolicitudEliminacion> solicitudes, Contribuyente contribuyente, boolean eliminado) {
        this.idHecho = idHecho;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.contenidoMultimedia = Optional.ofNullable(contenidoMultimedia);
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.etiquetas = etiquetas;
        this.solicitudes = solicitudes;
        this.contribuyente = contribuyente;
        this.eliminado = eliminado;

    }

}
