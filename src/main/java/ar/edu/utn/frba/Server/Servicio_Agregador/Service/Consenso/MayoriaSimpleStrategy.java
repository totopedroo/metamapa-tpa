package ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MayoriaSimpleStrategy implements AlgoritmoDeConsensoStrategy {
  @Override
  public void procesarYEstablecerConsenso(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
    long totalFuentes = todosLosHechos.stream()
            .map(Hecho::getTipoFuente)
            .distinct()
            .count();

    if (totalFuentes == 0) {
      hechoAProcesar.setConsensuado(Optional.of(false));
      return;
    }

    long fuentesCoincidentes = todosLosHechos.stream()
            .filter(h -> h.esIgualA(hechoAProcesar))
            .map(Hecho::getTipoFuente)
            .distinct()
            .count();

    boolean esConsensuado = (double) fuentesCoincidentes / totalFuentes >= 0.5;
    hechoAProcesar.setConsensuado(Optional.of(esConsensuado));
  }
}