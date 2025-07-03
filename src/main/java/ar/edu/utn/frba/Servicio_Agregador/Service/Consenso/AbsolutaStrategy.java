package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Domain.TipoFuente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class AbsolutaStrategy implements AlgoritmoDeConsensoStrategy {
  @Override
  public boolean tieneConsenso(Hecho hecho, List<Hecho> todos) {
    Set<TipoFuente> fuentesTotales = todos.stream()
            .map(Hecho::getTipoFuente)
            .collect(Collectors.toSet());

    Set<TipoFuente> fuentesCoincidentes = todos.stream()
            .filter(h -> h.esIgualA(hecho))
            .map(Hecho::getTipoFuente)
            .collect(Collectors.toSet());

    return fuentesCoincidentes.containsAll(fuentesTotales);
  }
}
