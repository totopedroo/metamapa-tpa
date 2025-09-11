package ar.edu.utn.frba.server.servicioAgregador.domain.consenso;

import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AbsolutaStrategy implements AlgoritmoDeConsensoStrategy {

  @Override public String codigo() { return "absoluta"; }

  @Override
  public ConsensoResult evaluar(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
    Set<TipoFuente> totalFuentesSet = todosLosHechos.stream()
            .map(Hecho::getTipoFuente)
            .collect(Collectors.toSet());

    if (totalFuentesSet.isEmpty()) {
      return ConsensoResult.builder()
              .estado(EstadoConsenso.INSUFICIENTE)
              .soportes(0)
              .totalFuentes(0)
              .fuentesSoporte(List.of())
              .conflictos(List.of())
              .build();
    }

    List<TipoFuente> fuentesSoporte = todosLosHechos.stream()
            .filter(h -> h.esIgualA(hechoAProcesar))
            .map(Hecho::getTipoFuente)
            .distinct()
            .collect(Collectors.toList());

    boolean ok = fuentesSoporte.size() == totalFuentesSet.size();

    return ConsensoResult.builder()
            .estado(ok ? EstadoConsenso.CONSENSUADO : EstadoConsenso.NO_CONSENSUADO)
            .soportes(fuentesSoporte.size())
            .totalFuentes(totalFuentesSet.size())
            .fuentesSoporte(fuentesSoporte)
            .conflictos(List.of())
            .build();
  }
}