package ar.edu.utn.frba.server.servicioAgregador.domain.navegacion;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.ConsensoService;
import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("curada")
public class CuradaStrategy implements ModoNavegacionStrategy {

    private final ConsensoService consensoService;
    private final TipoAlgoritmoConsenso algoritmoActivo;

    public CuradaStrategy(
            ConsensoService consensoService,
            @Value("${consenso.algoritmo-activo:mayoriaSimple}") String algoritmoActivoCodigo
    ) {
        this.consensoService = consensoService;
        this.algoritmoActivo = TipoAlgoritmoConsenso.fromCodigo(algoritmoActivoCodigo);
    }

    @Override
    public List<Hecho> filtrar(List<Hecho> hechos) {
        if (hechos == null || hechos.isEmpty()) return List.of();

        List<Hecho> consensuados = new ArrayList<>(hechos.size());
        for (Hecho h : hechos) {
            var res = consensoService.evaluarHecho(h, hechos, algoritmoActivo);
            if (res != null && res.getEstado() == EstadoConsenso.CONSENSUADO) {
                consensuados.add(h);
            }
        }
        return consensuados;
    }
}
