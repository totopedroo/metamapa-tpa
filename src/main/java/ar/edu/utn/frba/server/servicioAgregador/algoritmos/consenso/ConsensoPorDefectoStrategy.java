package ar.edu.utn.frba.server.servicioAgregador.algoritmos.consenso;

import ar.edu.utn.frba.server.common.enums.EstadoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsensoPorDefectoStrategy implements AlgoritmoDeConsensoStrategy {

    @Override public String codigo() { return "defecto"; }

    @Override
    public ConsensoResult evaluar(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
        int totalFuentes = (int) todosLosHechos.stream().map(Hecho::getFuente).distinct().count();
        return ConsensoResult.builder()
                .estado(EstadoConsenso.CONSENSUADO)
                .soportes(0)
                .totalFuentes(totalFuentes)
                .fuentesSoporte(List.of())
                .conflictos(List.of())
                .build();
    }
}
