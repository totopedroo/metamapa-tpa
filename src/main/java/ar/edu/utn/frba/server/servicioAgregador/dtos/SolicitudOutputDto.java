package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
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
    //private String mensajeError;

    // Constructor por defecto
    public SolicitudOutputDto() {
    }

    // Constructor con mensaje de error o texto
    public SolicitudOutputDto(String justificacion) {
        this.justificacion = justificacion;
        this.estado = EstadoDeSolicitud.RECHAZADA;
    }
}
