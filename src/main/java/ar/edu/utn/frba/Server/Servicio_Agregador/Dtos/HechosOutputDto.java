package ar.edu.utn.frba.Server.Servicio_Agregador.Dtos;


import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.ContenidoMultimedia;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Contribuyente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.SolicitudEliminacion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HechosOutputDto {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas;
    private Optional<Boolean> consensuado;

    public HechosOutputDto(Long idHecho, String titulo, String descripcion, String categoria, Optional<ContenidoMultimedia> contenidoMultimedia, Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, List<String> etiquetas, List<SolicitudEliminacion> solicitudes, Contribuyente contribuyente) {
    }

    public HechosOutputDto() {

    }


    public static HechosOutputDto fromModel(Hecho h) {
        HechosOutputDto dto = new HechosOutputDto();
        if (h == null) return dto;
        dto.setIdHecho(h.getIdHecho());
        dto.setTitulo(h.getTitulo());
        dto.setDescripcion(h.getDescripcion());
        dto.setCategoria(h.getCategoria());
        dto.setLatitud(h.getLatitud());
        dto.setLongitud(h.getLongitud());
        dto.setFechaAcontecimiento(h.getFechaAcontecimiento());
        dto.setEtiquetas(h.getEtiquetas());
        dto.setConsensuado(h.getConsensuado());
        return dto;
    }
}