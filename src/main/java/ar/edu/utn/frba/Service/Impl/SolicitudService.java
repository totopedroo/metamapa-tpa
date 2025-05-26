package ar.edu.utn.frba.Service.Impl;

import ar.edu.utn.frba.Dtos.SolicitudInputDto;
import ar.edu.utn.frba.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.Repository.ISolicitudRepository;
import ar.edu.utn.frba.Service.ISolicitudService;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.SolicitudEliminacion;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService implements ISolicitudService {

    @Autowired
    private IHechosRepository hechosRepository;

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Autowired
    private DetectorDeSpam detectorDeSpam;

    @SuppressWarnings("checkstyle:Indentation")
    @Override
    public SolicitudOutputDto crearSolicitud(SolicitudInputDto inputDto) {
        System.out.println("Buscando hecho con ID: " + inputDto.getIdHecho());
        Hecho hecho = hechosRepository.findById(inputDto.getIdHecho());
        System.out.println("Hecho encontrado: " + hecho.getTitulo());
        System.out.println("Longitud justificación: " + inputDto.getJustificacion().length());

        if (hecho == null) {
            throw new RuntimeException("No se encontró el hecho con ID: " + inputDto.getIdHecho());
        }

        try {
            if (detectorDeSpam.esSpam(inputDto.getJustificacion())) {
                SolicitudEliminacion solicitudSpam = new SolicitudEliminacion(inputDto.getJustificacion(), inputDto.getIdHecho());
                solicitudSpam.rechazarSolicitud();
                solicitudRepository.guardarSolicitud(solicitudSpam);

                SolicitudOutputDto dto = new SolicitudOutputDto();
                dto.setId(solicitudSpam.getIdSolicitud());
                dto.setEstado(solicitudSpam.getEstado());
                dto.setJustificacion(solicitudSpam.getJustificacion());

                return dto;
            }

            SolicitudEliminacion solicitud = new SolicitudEliminacion(inputDto.getJustificacion(), inputDto.getIdHecho());
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
}
