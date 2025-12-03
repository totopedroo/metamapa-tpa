package ar.edu.utn.frba.server.servicioAgregador.mappers;

import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;

import java.util.List;

// ColeccionMapper (ya lo tenés)
public class ColeccionMapper {

    public static Coleccion toDomain(ColeccionInputDto dto) {
        var criterios = (dto.getCriterioDePertenencia() == null)
                ? List.<CriterioDePertenencia>of()
                : dto.getCriterioDePertenencia().stream()
                .map(CriterioDePertenenciaMapper::toDomain)
                .toList();

        return new Coleccion(
                dto.getTitulo(),
                dto.getDescripcion(),
                criterios
        );
    }
}
