package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.enums.CampoHecho;
import ar.edu.utn.frba.server.contratos.enums.EstadoDeSolicitud;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.SolicitudModificacion;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudModificacionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.SolicitudModificacionOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.ISolicitudModificacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SolicitudModificacionService implements IsolicitudModificacionService {

    @Autowired
    private ISolicitudModificacionRepository solicitudRepo;
    @Autowired
    private IHechosRepository hechosRepository;


    // 1. Crear la solicitud (Cualquier usuario)
    @Transactional
    public void crearSolicitud(SolicitudModificacionInputDto dto) {
        Hecho hechoObtenido = hechosRepository.findById(dto.getIdHecho())
                .orElseThrow(() -> new NoSuchElementException("Hecho no encontrado"));

        SolicitudModificacion solicitud = SolicitudModificacion.builder()
                .hecho(hechoObtenido)
                .campo(dto.getCampo())
                .valorNuevo(dto.getValorNuevo())
                .justificacion(dto.getJustificacion())
                .idContribuyente(dto.getIdContribuyente())
                .estado(EstadoDeSolicitud.PENDIENTE)
                .fechaSolicitud(LocalDateTime.now())
                .build();

        solicitudRepo.save(solicitud);
    }

    // 2. Aprobar Solicitud (Solo Admin)
    @Transactional
    public void aprobarSolicitud(Long idSolicitud) {
        SolicitudModificacion solicitud = solicitudRepo.findById(idSolicitud)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada"));

        if (solicitud.getEstado() != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada");
        }

        // APLICAR EL CAMBIO AL HECHO REAL
        aplicarCambio(solicitud.getHecho(), solicitud.getCampo(), solicitud.getValorNuevo());

        // Actualizar estado de la solicitud
        solicitud.setEstado(EstadoDeSolicitud.ACEPTADA);
        solicitudRepo.save(solicitud);

        // Guardar el hecho modificado
        hechosRepository.save(solicitud.getHecho());
    }

    // En SolicitudModificacionService.java

    @Transactional
    public void rechazarSolicitud(Long idSolicitud) {
        SolicitudModificacion solicitud = solicitudRepo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (solicitud.getEstado() != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada");
        }

        solicitud.setEstado(EstadoDeSolicitud.RECHAZADA);
        solicitudRepo.save(solicitud);
    }

    public List<SolicitudModificacionOutputDto> findAllPendientes(){
        List<SolicitudModificacion> solicitudes = solicitudRepo.findByEstado(EstadoDeSolicitud.PENDIENTE);
        return solicitudes.stream().map(SolicitudModificacionOutputDto::fromModel).toList();
    }

    public void aplicarCambio(Hecho hecho, CampoHecho campo, String valorStr) {
        try {
            switch (campo) {
                case TITULO -> hecho.setTitulo(valorStr);
                case DESCRIPCION -> hecho.setDescripcion(valorStr);
                case CATEGORIA -> hecho.setCategoria(valorStr);
                case PROVINCIA -> hecho.setProvincia(valorStr);


                case LATITUD -> hecho.setLatitud(Double.parseDouble(valorStr));
                case LONGITUD -> hecho.setLongitud(Double.parseDouble(valorStr));


                case FECHA_ACONTECIMIENTO -> hecho.setFechaAcontecimiento(LocalDate.parse(valorStr));
                case HORA_ACONTECIMIENTO -> hecho.setHoraAcontecimiento(LocalTime.parse(valorStr));


                default -> throw new IllegalArgumentException("Campo no editable automáticamente: " + campo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir el valor '" + valorStr + "' para el campo " + campo, e);
        }
    }

    public List<SolicitudModificacionOutputDto> findAllSolicitudsByUser(Long idUser) {
        List<SolicitudModificacion> solicitudes = solicitudRepo.findByIdContribuyente(idUser);
        return solicitudes.stream().map(SolicitudModificacionOutputDto::fromModel).toList();
    }
}
