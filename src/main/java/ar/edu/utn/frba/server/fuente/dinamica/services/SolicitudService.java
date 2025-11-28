package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.domain.SolicitudEliminacion;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.SolicitudInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.SolicitudOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.mappers.ApiDinamicaMapper;
import ar.edu.utn.frba.server.fuente.dinamica.repositories.ISolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final ISolicitudRepository repo;
    private final ApiDinamicaMapper mapper;

    // Crear solicitud
    public SolicitudOutputDto crear(SolicitudInputDto input) {
        if (input == null || input.getIdHecho() == null ||
                input.getJustificacion() == null || input.getJustificacion().trim().length() < 50) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La justificación debe tener al menos 50 caracteres.");
        }

        SolicitudEliminacion s = mapper.toSolicitud(input); // <-- antes llamabas fromDto
        repo.save(s);                            // asigna ID y persiste en memoria
        return mapper.toDto(s);
    }

    // Aceptar solicitud
    public SolicitudOutputDto aceptar(Long idSolicitud) {
        SolicitudEliminacion s = buscarPorIdO404(idSolicitud);
        s.aceptarSolicitud();
        return mapper.toDto(s);
    }

    // Rechazar solicitud
    public SolicitudOutputDto rechazar(Long idSolicitud) {
        SolicitudEliminacion s = buscarPorIdO404(idSolicitud);
        s.rechazarSolicitud();
        return mapper.toDto(s);
    }

    // Listar todas
    public List<SolicitudOutputDto> listarTodas() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    // Helper
    private SolicitudEliminacion buscarPorIdO404(Long id) {
        if (id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idSolicitud es requerido");
        return repo.findAll().stream()
                .filter(s -> id.equals(s.getIdSolicitud()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));
    }
}
