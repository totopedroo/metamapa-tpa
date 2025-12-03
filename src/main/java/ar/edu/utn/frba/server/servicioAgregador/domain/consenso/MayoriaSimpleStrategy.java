package ar.edu.utn.frba.server.servicioAgregador.domain.consenso;
import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MayoriaSimpleStrategy implements AlgoritmoDeConsensoStrategy {

  @Override public String codigo() { return "mayoriaSimple"; }

  @Override
  public ConsensoResult evaluar(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
    int totalFuentes = (int) todosLosHechos.stream()
            .map(Hecho::getFuente)
            .distinct()
            .count();

    if (totalFuentes == 0) {
      return ConsensoResult.builder()
              .estado(EstadoConsenso.INSUFICIENTE)
              .soportes(0)
              .totalFuentes(0)
              .fuentesSoporte(List.of())
              .conflictos(List.of())
              .build();
    }

    @NotNull List<List<Fuente>> fuentesSoporte = todosLosHechos.stream()
            .filter(h -> h.esIgualA(hechoAProcesar))
            .map(Hecho::getFuente)
            .distinct()
            .collect(Collectors.toList());

    boolean ok = (double) fuentesSoporte.size() / totalFuentes >= 0.5;

    return ConsensoResult.builder()
            .estado(ok ? EstadoConsenso.CONSENSUADO : EstadoConsenso.NO_CONSENSUADO)
            .soportes(fuentesSoporte.size())
            .totalFuentes(totalFuentes)
            //.fuentesSoporte(fuentesSoporte)
            .conflictos(List.of())
            .build();
  }
}