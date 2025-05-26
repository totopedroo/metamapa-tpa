package ar.edu.utn.frba.Dtos;

import ar.edu.utn.frba.domain.ContenidoMultimedia;

import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.SolicitudEliminacion;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Data
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
    private List<String> etiquetas = new ArrayList();
    private List <SolicitudEliminacion> solicitudes= new ArrayList();

    public static HechosOutputDto fromModel(Hecho hecho) {
        HechosOutputDto dto = new HechosOutputDto();
        dto.idHecho = hecho.getIdHecho();
        dto.titulo = hecho.getTitulo();
        dto.descripcion = hecho.getDescripcion();
        dto.categoria = hecho.getCategoria();
        dto.latitud = hecho.getLatitud();
        dto.longitud = hecho.getLongitud();

        return dto;
    }
}
