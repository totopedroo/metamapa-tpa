package ar.edu.utn.frba.server.fuente.estatica.mappers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.common.dtos.HechoDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
                h.getFechaAcontecimiento() != null ? h.getFechaAcontecimiento() : LocalDate.now()
        );
    }
}
