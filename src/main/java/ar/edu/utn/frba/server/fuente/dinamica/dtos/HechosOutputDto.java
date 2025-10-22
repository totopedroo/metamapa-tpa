package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import ar.edu.utn.frba.server.fuente.dinamica.domain.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class HechosOutputDto {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private ContenidoMultimediaDto contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private List<SolicitudEliminacionDto> solicitudes;
    private ContribuyenteDto contribuyente;
    private boolean eliminado;
}
