package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SolicitudEliminacionDto {
    private Long idSolicitud;
    private Long idHecho;      // id del hecho asociado
    private EstadoDeSolicitud estado;     // "PENDIENTE" | "ACEPTADA" | "RECHAZADA"
    private String justificacion;
}

