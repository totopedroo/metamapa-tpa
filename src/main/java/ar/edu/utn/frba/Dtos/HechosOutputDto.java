package ar.edu.utn.frba.Dtos;

import ar.edu.utn.frba.domain.ContenidoMultimedia;
import ar.edu.utn.frba.domain.Contribuyente;
import ar.edu.utn.frba.domain.SolicitudEliminacion;
import lombok.Data;
import ar.edu.utn.frba.domain.Hecho;
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
    public HechosOutputDto() {
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
        return dto;
    }

    public HechosOutputDto(Long idHecho, String titulo, String descripcion, String categoria,
                           ContenidoMultimedia contenidoMultimedia, Double latitud, Double longitud,
                           LocalDate fechaAcontecimiento, LocalDate fechaCarga,
                           List<String> etiquetas, List<SolicitudEliminacion> solicitudes, Contribuyente contribuyente) {
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

    }

}
