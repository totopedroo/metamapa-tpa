package ar.edu.utn.frba.server.servicioAgregador.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SolicitudInputDto {
    private Long idHecho;
    private Long idUsuario;
    private String justificacion;

}
