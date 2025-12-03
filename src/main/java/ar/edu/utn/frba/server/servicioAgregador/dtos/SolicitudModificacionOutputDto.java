package ar.edu.utn.frba.server.servicioAgregador.dtos;


import ar.edu.utn.frba.server.contratos.enums.CampoHecho;
import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudModificacion;
import lombok.Data;

@Data
public class SolicitudModificacionOutputDto {
    private Long id;
    private Long idHecho;
    private Long idContribuyente;
    private CampoHecho campo;
    private EstadoDeSolicitud estado;
    private String valorNuevo; // El dato nuevo en formato string
    private String justificacion;


    public static SolicitudModificacionOutputDto fromModel(SolicitudModificacion s) {
        SolicitudModificacionOutputDto dto = new SolicitudModificacionOutputDto();
        if (s == null) return dto;
        dto.setId(s.getId());
        dto.setIdHecho(s.getHecho().getIdHecho());
        dto.setCampo(s.getCampo());
        dto.setValorNuevo(s.getValorNuevo());
        dto.setJustificacion(s.getJustificacion());
        dto.setIdContribuyente(s.getIdContribuyente());
        dto.setEstado(s.getEstado());
        return dto;
    }


}

