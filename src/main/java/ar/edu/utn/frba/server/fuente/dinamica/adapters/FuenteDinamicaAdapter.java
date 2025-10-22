package ar.edu.utn.frba.server.fuente.dinamica.adapters;

import ar.edu.utn.frba.server.fuente.dinamica.mappers.DinamicaMapper;
import ar.edu.utn.frba.server.fuente.domain.FuentePort;
import ar.edu.utn.frba.server.common.dtos.HechoDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.services.HechosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FuenteDinamicaAdapter implements FuentePort {

    private final HechosService hechosService;
    private final DinamicaMapper mapper;

    @Override
    public List<HechoDto> importar(String criterio) {
        List<Hecho> hechos = hechosService.buscar(criterio);
        return hechos.stream().map(mapper::toHechoDto).toList();
    }
}
