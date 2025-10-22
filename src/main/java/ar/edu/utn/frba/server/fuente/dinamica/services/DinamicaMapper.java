package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DinamicaMapper {

    public HechoDto toHechoDto(Hecho h) {
        return new HechoDto(
                h.getIdHecho(),
                h.getTitulo(),
                h.getDescripcion(),
                "fuente-dinamica",
                h.getCategoria(),
                h.getLatitud(),
                h.getLongitud(),
                h.getFechaAcontecimiento() != null ? h.getFechaAcontecimiento() : LocalDate.now()
        );
    }
}
