package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MultiplesMencionesStrategy implements AlgoritmoDeConsensoStrategy {

  @Override
  public List<Hecho> obtenerHechosConsensuados(List<List<Hecho>> hechosPorFuente) {
    Map<String, List<Hecho>> agrupadosPorTitulo = new HashMap<>();

    for (List<Hecho> fuente : hechosPorFuente) {
      for (Hecho h : fuente) {
        String titulo = h.getTitulo().toLowerCase().trim();
        agrupadosPorTitulo.computeIfAbsent(titulo, k -> new ArrayList<>()).add(h);
      }
    }

    List<Hecho> consensuados = new ArrayList<>();

    for (Map.Entry<String, List<Hecho>> entry : agrupadosPorTitulo.entrySet()) {
      List<Hecho> hechos = entry.getValue();

      // Si hay al menos 2 menciones y no hay conflictos entre ellos
      if (hechos.size() >= 2 && todosIguales(hechos)) {
        consensuados.add(hechos.get(0));
      }
    }

    return consensuados;
  }

  private boolean todosIguales(List<Hecho> hechos) {
    if (hechos.isEmpty())
      return true;
    Hecho primero = hechos.get(0);
    return hechos.stream().allMatch(h -> h.getCategoria().equals(primero.getCategoria())
        && h.getLatitud().equals(primero.getLatitud())
        && h.getLongitud().equals(primero.getLongitud()));
  }
}
