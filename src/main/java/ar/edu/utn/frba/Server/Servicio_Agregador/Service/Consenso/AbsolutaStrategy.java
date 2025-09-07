package ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.TipoFuente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
@Component
public class AbsolutaStrategy implements AlgoritmoDeConsensoStrategy {
  @Override
  public void procesarYEstablecerConsenso(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
    List<Fuente> fuentestotales = new ArrayList<>();

    todosLosHechos.stream().flatMap(h => fuentestotales.add(hechoAProcesar.getFuente()));

    List<Fuente> fuentesTotales = todosLosHechos.stream()
            .flatMap(Hecho::getFuente()).collect.(Collectors.toList());

    if (fuentesTotales.isEmpty()) {
      hechoAProcesar.setConsensuado(Optional.of(false));
      return;
    }

    Set<TipoFuente> fuentesCoincidentes = todosLosHechos.stream()
            .filter(h -> h.esIgualA(hechoAProcesar))
            .map(Hecho::getTipoFuente)
            .collect(Collectors.toSet());

    boolean esConsensuado = fuentesCoincidentes.equals(fuentesTotales);
    hechoAProcesar.setConsensuado(Optional.of(esConsensuado));
  }
}

