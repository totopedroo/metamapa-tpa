package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.domain.Hecho;

import java.util.*;

public class MayoriaSimpleStrategy implements AlgoritmoDeConsensoStrategy {

  @Override
  public List<Hecho> obtenerHechosConsensuados(List<List<Hecho>> hechosPorFuente) {
    Map<String, Integer> contador = new HashMap<>();
    Map<String, Hecho> hechosUnicos = new HashMap<>();

    for (List<Hecho> fuente : hechosPorFuente) {
      for (Hecho h : fuente) {
        String clave = h.getTitulo().toLowerCase().trim(); // criterio simple
        contador.put(clave, contador.getOrDefault(clave, 0) + 1);
        hechosUnicos.putIfAbsent(clave, h);
      }
    }

    int cantidadFuentes = hechosPorFuente.size();
    List<Hecho> consensuados = new ArrayList<>();

    for (String clave : contador.keySet()) {
      if (contador.get(clave) >= Math.ceil(cantidadFuentes / 2.0)) {
        consensuados.add(hechosUnicos.get(clave));
      }
    }

    return consensuados;
  }
}
