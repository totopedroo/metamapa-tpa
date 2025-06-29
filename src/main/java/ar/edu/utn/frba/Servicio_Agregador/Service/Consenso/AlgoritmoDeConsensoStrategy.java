package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import java.util.List;

public interface AlgoritmoDeConsensoStrategy {
    List<Hecho> obtenerHechosConsensuados(List<List<Hecho>> hechosPorFuente);
}
