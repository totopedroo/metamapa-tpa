package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class SolicitudOutputDto {
    private Long id;
    private Long idHecho;
    private EstadoDeSolicitud estado;
    private String justificacion;
}
