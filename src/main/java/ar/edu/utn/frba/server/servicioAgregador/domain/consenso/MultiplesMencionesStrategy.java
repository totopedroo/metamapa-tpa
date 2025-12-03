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
public class MultiplesMencionesStrategy implements AlgoritmoDeConsensoStrategy {

  @Override public String codigo() { return "multiplesMenciones"; }

  @Override
  public ConsensoResult evaluar(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
    int totalFuentes = (int) todosLosHechos.stream()
            .map(Hecho::getFuente)
            .distinct()
            .count();

    @NotNull List<List<Fuente>> fuentesSoporte = todosLosHechos.stream()
            .filter(h -> h.esIgualA(hechoAProcesar))
            .map(Hecho::getFuente)
            .distinct()
            .collect(Collectors.toList());

    List<String> conflictos = todosLosHechos.stream()
            .filter(h -> h.getTitulo().equalsIgnoreCase(hechoAProcesar.getTitulo()) && !h.esIgualA(hechoAProcesar))
            .map(Hecho::getTitulo) // si tenés ID, mejor usarlo acá
            .distinct()
            .collect(Collectors.toList());

    if (!conflictos.isEmpty()) {
      return ConsensoResult.builder()
              .estado(EstadoConsenso.CONFLICTO)
              .soportes(fuentesSoporte.size())
              .totalFuentes(totalFuentes)
           //   .fuentesSoporte(fuentesSoporte)
              .conflictos(conflictos)
              .build();
    }

    boolean ok = fuentesSoporte.size() >= 2;

    return ConsensoResult.builder()
            .estado(ok ? EstadoConsenso.CONSENSUADO : EstadoConsenso.NO_CONSENSUADO)
            .soportes(fuentesSoporte.size())
            .totalFuentes(totalFuentes)
       //     .fuentesSoporte(fuentesSoporte) fijarse esto
            .conflictos(List.of())
            .build();
  }
}