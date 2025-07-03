package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MultiplesMencionesStrategy implements AlgoritmoDeConsensoStrategy {
  @Override
  public boolean tieneConsenso(Hecho hecho, List<Hecho> todos) {
    long coincidencias = todos.stream()
            .filter(h -> h.esIgualA(hecho) && h != hecho)
            .map(Hecho::getTipoFuente)
            .distinct()
            .count();

    boolean hayConflicto = todos.stream()
            .filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()) && !h.equals(hecho) && !h.esIgualA(hecho))
            .findAny()
            .isPresent();

    return coincidencias >= 1 && !hayConflicto;
  }
}
