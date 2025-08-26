package ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SolicitudInputDto {
    private Long idHecho;
    private String justificacion;

}
