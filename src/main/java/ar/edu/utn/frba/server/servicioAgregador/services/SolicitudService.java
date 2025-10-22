package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.common.services.IDetectorDeSpam;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudOutputDto;
import ar.edu.utn.frba.server.common.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.ISolicitudRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudEliminacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("solicitudAgregadorService")
public class SolicitudService implements ISolicitudService {

    @Autowired
    private IHechosRepository hechosRepository;

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Autowired
    private IDetectorDeSpam detectorDeSpam;

    @Override
    public SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto) {
        Hecho hecho = hechosRepository.findById(inputDto.getIdHecho()).orElse(null);
        if (hecho == null) throw new RuntimeException("No se encontró el hecho con ID: " + inputDto.getIdHecho());

        String justif = inputDto.getJustificacion() == null ? "" : inputDto.getJustificacion().trim();
        boolean justificacionSpam = hecho.getSolicitudes().stream().anyMatch(s ->
                detectorDeSpam.justificacionRepetida(
                        s.getJustificacion() == null ? "" : s.getJustificacion(),
                        justif
                )
        );

        // Auto-rechazo de spam
        if (detectorDeSpam.esSpam(justif) || justificacionSpam) {
            SolicitudEliminacion solicitudSpam = new SolicitudEliminacion(justif, inputDto.getIdHecho());
            solicitudSpam.rechazarPorSpam();
            solicitudRepository.save(solicitudSpam);

            var dto = new SolicitudOutputDto();
            dto.setId(solicitudSpam.getIdSolicitud());
            dto.setEstado(solicitudSpam.getEstado());
            dto.setJustificacion(solicitudSpam.getJustificacion());
            return dto;
        }

        // PENDIENTE por defecto
        SolicitudEliminacion solicitud = new SolicitudEliminacion(justif, inputDto.getIdHecho());
        hecho.agregarSolicitud(solicitud);

        solicitudRepository.save(solicitud);
        hechosRepository.save(hecho);

        var dto = new SolicitudOutputDto();
        dto.setId(solicitud.getIdSolicitud());
        dto.setEstado(solicitud.getEstado());
        dto.setJustificacion(solicitud.getJustificacion());
        return dto;
    }

    @Override
    public void aceptarSolicitud(long idSolicitud) {
        var solicitud = solicitudRepository.findAll().stream()
                .filter(s -> s.getIdSolicitud() == idSolicitud)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró la solicitud con ID: " + idSolicitud));

        if (solicitud.getEstado() != EstadoDeSolicitud.PENDIENTE)
            throw new RuntimeException("Solo se puede aceptar una solicitud PENDIENTE");

        solicitud.aceptarSolicitud();
        solicitudRepository.save(solicitud);

        Hecho hecho = hechosRepository.findById(solicitud.getIdHechoAsociado()).orElse(null);
        if (hecho != null) {
            hecho.marcarComoEliminado();      // explícito
            hechosRepository.save(hecho);     // persistir cambio
        }
    }

    @Override
    public void rechazarSolicitud(long idSolicitud) {
        var solicitud = solicitudRepository.findAll().stream()
                .filter(s -> s.getIdSolicitud() == idSolicitud)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró la solicitud con ID: " + idSolicitud));

        if (solicitud.getEstado() != EstadoDeSolicitud.PENDIENTE)
            throw new RuntimeException("Solo se puede rechazar una solicitud PENDIENTE");

        solicitud.rechazarSolicitud();
        solicitudRepository.save(solicitud);
    }

}
