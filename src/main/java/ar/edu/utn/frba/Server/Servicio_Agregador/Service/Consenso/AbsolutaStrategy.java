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

    Set<Fuente> fuentesTotales = todosLosHechos.stream()
            .flatMap(hecho -> hecho.getFuente().stream())
            .collect(Collectors.toSet());

    if (fuentesTotales.isEmpty()) {
      hechoAProcesar.setConsensuado(Optional.of(false));
      return;
    }
    boolean esConsensuado = hechoAProcesar.getFuente().equals(fuentesTotales);
    hechoAProcesar.setConsensuado(Optional.of(esConsensuado));
  }
}

