package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.enums.CampoHecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudModificacionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudModificacionOutputDto;

import java.util.List;

public interface IsolicitudModificacionService {
     void aplicarCambio(Hecho hecho, CampoHecho campo, String valorStr);
     void aprobarSolicitud(Long idSolicitud);
     void crearSolicitud(SolicitudModificacionInputDto dto);
     void rechazarSolicitud(Long idSolicitud);
     List<SolicitudModificacionOutputDto> findAllPendientes();
     List<SolicitudModificacionOutputDto> findAllSolicitudsByUser(Long idUser);
}
