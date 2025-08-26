package ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MultiplesMencionesStrategy implements AlgoritmoDeConsensoStrategy {
  @Override
  public void procesarYEstablecerConsenso(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
    long mencionesDelMismoHecho = todosLosHechos.stream()
            .filter(h -> h.esIgualA(hechoAProcesar))
            .map(Hecho::getTipoFuente)
            .distinct()
            .count();

    boolean hayConflicto = todosLosHechos.stream()
            .anyMatch(h -> h.getTitulo().equalsIgnoreCase(hechoAProcesar.getTitulo()) && !h.esIgualA(hechoAProcesar));

    boolean esConsensuado = mencionesDelMismoHecho >= 2 && !hayConflicto;
    hechoAProcesar.setConsensuado(Optional.of(esConsensuado));
  }
}