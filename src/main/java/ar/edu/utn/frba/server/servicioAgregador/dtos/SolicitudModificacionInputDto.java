package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.contratos.enums.CampoHecho;
import lombok.Data;

@Data
public class SolicitudModificacionInputDto {
    private Long idHecho;
    private Long idContribuyente;
    private CampoHecho campo; // "TITULO", "LATITUD", etc.
    private String valorNuevo; // El dato nuevo en formato string
    private String justificacion;
}