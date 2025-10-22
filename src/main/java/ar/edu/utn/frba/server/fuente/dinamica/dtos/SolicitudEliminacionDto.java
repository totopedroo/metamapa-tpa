package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import ar.edu.utn.frba.server.common.enums.EstadoDeSolicitud;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SolicitudEliminacionDto {
    private Long idSolicitud;
    private Long idHecho;      // id del hecho asociado
    private EstadoDeSolicitud estado;     // "PENDIENTE" | "ACEPTADA" | "RECHAZADA"
    private String justificacion;
}

