package ar.edu.utn.frba.server.fuente.proxy.services;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Component
public class DesastresApiMapper {

    public Hecho toHecho(DesastreDto dto) {
        LocalDate fechaAcontecimiento = null;
        LocalTime horaAcontecimiento = null;

        OffsetDateTime odt = dto.getFechaHecho();
        if (odt != null) {
            fechaAcontecimiento = odt.toLocalDate();
            horaAcontecimiento = odt.toLocalTime();
        }

        return Hecho.builder()
                .titulo(nvl(dto.getTitulo()))
                .descripcion(nvl(dto.getDescripcion()))
                .categoria(nvl(dto.getCategoria()))
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .provincia(null) // la API no trae provincia
                .fechaAcontecimiento(fechaAcontecimiento)
                .horaAcontecimiento(horaAcontecimiento)
                .fechaCarga(LocalDate.now())
                // contenidoMultimedia = null (la API no trae url)
                .build();
    }

    private String nvl(String v) { return v == null ? "" : v; }
}