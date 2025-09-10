package ar.edu.utn.frba.Server.Servicio_Agregador.Dtos;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS) // mostrará nulls (provincia, hora, fechaCarga, fuente) si faltan
public class HechosOutputDto {

    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;

    private Optional<ContenidoMultimedia> contenidoMultimedia;

    private Double latitud;
    private Double longitud;

    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;

    private List<String> etiquetas;
    private List<SolicitudEliminacion> solicitudes;

    private Contribuyente contribuyente;
    private boolean eliminado;
    private Optional<Boolean> consensuado;

    private TipoFuente fuente;

    // >>> Para estadísticas y salida
    private String provincia;
    private Integer hora;


    public HechosOutputDto(Long idHecho, String titulo, String descripcion, String categoria, Optional<ContenidoMultimedia> contenidoMultimedia, Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, List<String> etiquetas, List<SolicitudEliminacion> solicitudes, String provincia, Integer hora) {
    }

    public HechosOutputDto() {
        
    }

    public HechosOutputDto(Long idHecho, String titulo, String descripcion, String categoria, Optional<ContenidoMultimedia> contenidoMultimedia, Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, List<String> etiquetas, List<SolicitudEliminacion> solicitudes, Contribuyente contribuyente) {
    }

    public static HechosOutputDto fromModel(Hecho h) {
        HechosOutputDto dto = new HechosOutputDto();
        if (h == null) return dto;

        dto.setIdHecho(h.getIdHecho());
        dto.setTitulo(h.getTitulo());
        dto.setDescripcion(h.getDescripcion());
        dto.setCategoria(h.getCategoria());
        dto.setContenidoMultimedia(h.getContenidoMultimedia());
        dto.setLatitud(h.getLatitud());
        dto.setLongitud(h.getLongitud());
        dto.setFechaAcontecimiento(h.getFechaAcontecimiento());
        dto.setFechaCarga(h.getFechaCarga());
        dto.setEtiquetas(h.getEtiquetas());
        dto.setSolicitudes(h.getSolicitudes());
        dto.setContribuyente(h.getContribuyente());
        dto.setEliminado(h.isEliminado());
        dto.setConsensuado(h.getConsensuado());
        dto.setFuente(h.getFuente());
        dto.setProvincia(h.getProvincia());
        dto.setHora(h.getHora());


        return dto;
    }
}
