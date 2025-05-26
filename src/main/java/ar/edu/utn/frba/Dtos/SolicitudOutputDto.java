package ar.edu.utn.frba.Dtos;

import ar.edu.utn.frba.Enums.EstadoDeSolicitud;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SolicitudOutputDto {
    private Long id;
    private EstadoDeSolicitud estado;
    private String justificacion;
}
