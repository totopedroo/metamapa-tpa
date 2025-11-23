package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudEliminacion;
import lombok.Data;

@Data
public class SolicitudFrontDto {
        private Long id;
        private EstadoDeSolicitud estado;
        private String justificacion;
        private String tituloHecho;



}
