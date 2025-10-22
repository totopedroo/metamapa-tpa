package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import ar.edu.utn.frba.server.fuente.dinamica.domain.SolicitudEliminacion;
import ar.edu.utn.frba.server.fuente.dinamica.domain.ContenidoMultimedia;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Contribuyente;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiDinamicaMapper {

    // ===== DTO -> dominio =====
    public ContenidoMultimedia toContenidoMultimedia(ContenidoMultimediaDto dto) {
        if (dto == null) return null;
        var cm = new ContenidoMultimedia();
        cm.url = dto.getUrl();
        return cm;
    }

    public Contribuyente toContribuyente(ContribuyenteDto dto) {
        if (dto == null) return null;
        var c = new Contribuyente();
        c.setNombre(dto.getNombre());
        c.setApellido(dto.getApellido());
        c.setFechaNacimiento(dto.getFechaNacimiento());
        return c;
    }

    public SolicitudEliminacion toSolicitud(SolicitudInputDto in) {   // <-- renombrado
        if (in == null) return null;
        return new SolicitudEliminacion(in.getJustificacion(), in.getIdHecho());
    }

    // ===== dominio -> DTO =====
    public HechosOutputDto toOutput(Hecho h) {
        return HechosOutputDto.builder()
                .idHecho(h.getIdHecho())
                .titulo(h.getTitulo())
                .descripcion(h.getDescripcion())
                .categoria(h.getCategoria())
                .contenidoMultimedia(toDto(h.getContenidoMultimedia()))
                .latitud(h.getLatitud())
                .longitud(h.getLongitud())
                .fechaAcontecimiento(h.getFechaAcontecimiento())
                .fechaCarga(h.getFechaCarga())

                .solicitudes(toDtoLista(h.getSolicitudes()))
                .contribuyente(toDto(h.getContribuyente()))
                .eliminado(h.isEliminado())
                .build();
    }

    public SolicitudOutputDto toDto(SolicitudEliminacion s) {
        var dto = new SolicitudOutputDto();
        dto.setId(s.getIdSolicitud());
        dto.setIdHecho(s.getIdHechoAsociado());
        dto.setEstado(s.getEstado());             // enum o String, según tu DTO
        dto.setJustificacion(s.getJustificacion());
        return dto;
    }

    // --- helpers dominio -> DTO (privados) ---
    private ContenidoMultimediaDto toDto(ContenidoMultimedia cm) {
        if (cm == null) return null;
        return ContenidoMultimediaDto.builder().url(cm.url).build();
    }

    private ContribuyenteDto toDto(Contribuyente c) {
        if (c == null) return null;
        return ContribuyenteDto.builder()
                .nombre(c.getNombre())
                .apellido(c.getApellido())
                .fechaNacimiento(c.getFechaNacimiento())
                .build();
    }

    private List<SolicitudEliminacionDto> toDtoLista(List<SolicitudEliminacion> src) {
        if (src == null) return List.of();
        return src.stream()
                .map(s -> SolicitudEliminacionDto.builder()
                        .idSolicitud(s.getIdSolicitud())
                        .idHecho(s.getIdHechoAsociado())
                        .estado(s.getEstado())          // si tu DTO usa String, cambiar por .estado(s.getEstado().name())
                        .justificacion(s.getJustificacion())
                        .build())
                .collect(Collectors.toList());
    }
}