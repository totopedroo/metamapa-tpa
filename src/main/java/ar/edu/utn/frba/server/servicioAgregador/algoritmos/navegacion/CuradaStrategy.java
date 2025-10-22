package ar.edu.utn.frba.server.servicioAgregador.algoritmos.navegacion;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.algoritmos.consenso.ConsensoService;
import ar.edu.utn.frba.server.common.enums.EstadoConsenso;
import ar.edu.utn.frba.server.common.enums.TipoAlgoritmoConsenso;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("curada")
@RequiredArgsConstructor
public class CuradaStrategy implements ModoNavegacionStrategy {

    private final ConsensoService consensoService;

    // lee de config; default a "defecto"
    @Value("${consenso.algoritmo-activo:defecto}")
    private String algoritmoActivoCodigo;

    @Override
    public List<Hecho> filtrar(List<Hecho> universo) {
        if (universo == null || universo.isEmpty()) return List.of();

        // qué algoritmos calculás
        var algoritmos = java.util.EnumSet.of(
                TipoAlgoritmoConsenso.MAYORIA_SIMPLE,
                TipoAlgoritmoConsenso.MULTIPLES_MENCIONES,
                TipoAlgoritmoConsenso.ABSOLUTA,
                TipoAlgoritmoConsenso.DEFECTO
        );

        var algActivo = TipoAlgoritmoConsenso.fromCodigo(algoritmoActivoCodigo);

        return universo.stream()
                .filter(h -> {
                    var res = consensoService.evaluarHecho(h, universo, algoritmos);
                    var r = res.get(algActivo);
                    return r != null && r.getEstado() == EstadoConsenso.CONSENSUADO;
                })
                .toList();

    }
}
