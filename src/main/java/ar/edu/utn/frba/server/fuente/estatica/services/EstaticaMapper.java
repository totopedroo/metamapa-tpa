package ar.edu.utn.frba.server.fuente.estatica.services;

import ar.edu.utn.frba.server.fuente.estatica.domain.Hecho;
import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class EstaticaMapper {

    public HechoDto toHechoDto(Hecho h) {
        return new HechoDto(
                h.getIdHecho(),
                h.getTitulo(),
                h.getDescripcion(),
                "fuente-estatica",
                h.getCategoria(),
                h.getLatitud(),
                h.getLongitud(),
                h.getFechaAcontecimiento() != null ? h.getFechaAcontecimiento() : LocalDate.now(),
                h.getEtiquetas() != null ? h.getEtiquetas() : List.of()
        );
    }
}
