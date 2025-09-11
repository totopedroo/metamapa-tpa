package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.CriterioDePertenenciaDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// ColeccionMapper (ya lo tenés)
public class ColeccionMapper {

    public static Coleccion toDomain(ColeccionInputDto dto) {
        var criterios = (dto.getCriterioDePertenencia() == null)
                ? List.<CriterioDePertenencia>of()
                : dto.getCriterioDePertenencia().stream()
                .map(CriterioDePertenenciaMapper::toDomain)
                .toList();

        return new Coleccion(
                java.util.UUID.randomUUID().toString(),
                dto.getTitulo(),
                dto.getDescripcion(),
                criterios
        );
    }
}
