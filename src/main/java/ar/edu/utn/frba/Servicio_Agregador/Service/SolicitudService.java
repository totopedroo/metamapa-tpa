package ar.edu.utn.frba.Servicio_Agregador.Service;

import ar.edu.utn.frba.Servicio_Agregador.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Enums.EstadoDeSolicitud;
import ar.edu.utn.frba.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Servicio_Agregador.Repository.ISolicitudRepository;
import ar.edu.utn.frba.Servicio_Agregador.Service.IDetectorDeSpam;
import ar.edu.utn.frba.Servicio_Agregador.Service.ISolicitudService;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Domain.SolicitudEliminacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("solicitudAgregadorService")
public class SolicitudService implements ISolicitudService {

    @Autowired
    @Qualifier("hechosAgregadorRepository")
    private IHechosRepository hechosRepository;

    @Autowired
    @Qualifier("solicitudAgregadorRepository")
    private ISolicitudRepository solicitudRepository;

    @Autowired
    @Qualifier("detectorSpamAgregador")
    private IDetectorDeSpam detectorDeSpam;

    @SuppressWarnings("checkstyle:Indentation")
    @Override
    public SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto) {
        Hecho hecho = hechosRepository.findById(inputDto.getIdHecho());
        if (hecho == null) {
            throw new RuntimeException("No se encontró el hecho con ID: " + inputDto.getIdHecho());
        }

        System.out.println("Hecho encontrado: " + hecho.getTitulo());
        System.out.println("Longitud justificación: " + inputDto.getJustificacion().length());

        boolean justificacionSpam = false;
        if (hecho.getSolicitudes().size() > 0) {
            justificacionSpam = hecho.getSolicitudes().stream().anyMatch(solicitud -> detectorDeSpam
                    .justificacionRepetida(solicitud.getJustificacion(), inputDto.getJustificacion()));

        }
        ;
        try {
            if (detectorDeSpam.esSpam(inputDto.getJustificacion()) || justificacionSpam) {
                SolicitudEliminacion solicitudSpam = new SolicitudEliminacion(inputDto.getJustificacion(),
                        inputDto.getIdHecho());
                solicitudSpam.rechazarSolicitud();
                solicitudRepository.guardarSolicitud(solicitudSpam);

                SolicitudOutputDto dto = new SolicitudOutputDto();
                dto.setId(solicitudSpam.getIdSolicitud());
                dto.setEstado(solicitudSpam.getEstado());
                dto.setJustificacion(solicitudSpam.getJustificacion());

                return dto;
            }

            SolicitudEliminacion solicitud = new SolicitudEliminacion(inputDto.getJustificacion(),
                    inputDto.getIdHecho());
            hecho.agregarSolicitud(solicitud);

            solicitudRepository.guardarSolicitud(solicitud);

            SolicitudOutputDto dto = new SolicitudOutputDto();
            dto.setId(solicitud.getIdSolicitud());
            dto.setEstado(solicitud.getEstado());
            dto.setJustificacion(solicitud.getJustificacion());
            System.out.println("Solicitud creada con ID: " + dto.getId());

            return dto;
        } catch (Exception e) {
            System.out.println("Error al crear solicitud: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void aceptarSolicitud(long idSolicitud) {
        SolicitudEliminacion solicitud = solicitudRepository.findAll().stream()
                .filter(s -> s.getIdSolicitud() == idSolicitud)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró la solicitud con ID: " + idSolicitud));

        if (solicitud.getEstado() == EstadoDeSolicitud.ACEPTADA) {
            throw new RuntimeException("No se puede aceptar una solicitud que ya fue ACEPTADA.");
        } else if (solicitud.getEstado() == EstadoDeSolicitud.RECHAZADA) {
            throw new RuntimeException("No se puede aceptar una solicitud que ya fue RECHAZADA.");
        }

        solicitud.aceptarSolicitud();

        Hecho hecho = hechosRepository.findById(solicitud.getIdHechoAsociado());
        if (hecho != null) {
            hecho.verificarEliminacion();
        }
    }

    @Override
    public void rechazarSolicitud(long idSolicitud) {
        SolicitudEliminacion solicitud = solicitudRepository.findAll().stream()
                .filter(s -> s.getIdSolicitud() == idSolicitud)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró la solicitud con ID: " + idSolicitud));

        if (solicitud.getEstado() == EstadoDeSolicitud.ACEPTADA) {
            throw new RuntimeException("No se puede rechazar una solicitud que ya fue ACEPTADA.");
        } else if (solicitud.getEstado() == EstadoDeSolicitud.RECHAZADA) {
            throw new RuntimeException("No se puede rechazar una solicitud que ya fue RECHAZADA.");
        }

        solicitud.rechazarSolicitud();

        Hecho hecho = hechosRepository.findById(solicitud.getIdHechoAsociado());
        if (hecho != null) {
            hecho.verificarEliminacion();
        }
    }

}
