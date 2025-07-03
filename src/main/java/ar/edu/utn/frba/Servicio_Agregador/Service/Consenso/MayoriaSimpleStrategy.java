package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class MayoriaSimpleStrategy implements AlgoritmoDeConsensoStrategy {
  @Override
  public boolean tieneConsenso(Hecho hecho, List<Hecho> todos) {
    long fuentesConCoincidencia = todos.stream()
            .filter(h -> h.esIgualA(hecho))
            .map(Hecho::getTipoFuente)
            .distinct()
            .count();

    long totalFuentes = todos.stream()
            .map(Hecho::getTipoFuente)
            .distinct()
            .count();

    return fuentesConCoincidencia >= Math.ceil(totalFuentes / 2.0);
  }
}

