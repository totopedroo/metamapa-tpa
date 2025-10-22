package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import ar.edu.utn.frba.server.common.enums.EstadoDeSolicitud;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class SolicitudOutputDto {
    private Long id;
    private Long idHecho;
    private EstadoDeSolicitud estado;
    private String justificacion;
}
