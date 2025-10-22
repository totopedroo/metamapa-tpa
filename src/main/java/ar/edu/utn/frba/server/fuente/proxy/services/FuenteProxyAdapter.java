package ar.edu.utn.frba.server.fuente.proxy.services;

import ar.edu.utn.frba.server.common.dtos.HechoDto;
import ar.edu.utn.frba.server.fuente.domain.FuentePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class FuenteProxyAdapter implements FuentePort {
    private final DesastresClient desastresClient;
    private final MetaMapaClient metaMapaClient;
    private final ProxyMapper mapper;

    @Value("${proxy.desastres.enabled:true}")
    private boolean desastresEnabled;

    @Value("${proxy.metamapa.enabled:true}")
    private boolean metamapaEnabled;

    @Value("${proxy.metamapa.base-url:http://otra-instancia.com}")
    private String metaBaseUrl;

    @Override
    public List<HechoDto> importar(String criterio) {
        Stream<HechoDto> s1 = Stream.empty();
        Stream<HechoDto> s2 = Stream.empty();

        if (desastresEnabled) {
            s1 = desastresClient.buscar(criterio).stream().map(mapper::toHechoDto);
        }
        if (metamapaEnabled) {
            s2 = metaMapaClient.buscarTodos(metaBaseUrl).stream().map(mapper::toHechoDtoMeta);
        }

        return Stream.concat(s1, s2)
                .filter(h -> mapper.match(h, criterio)) // por las dudas si el client no filtró
                .toList();
    }
}
