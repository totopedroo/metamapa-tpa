package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.domain.Hecho;
import java.util.List;

public interface AlgoritmoDeConsensoStrategy {
    List<Hecho> obtenerHechosConsensuados(List<List<Hecho>> hechosPorFuente);
}
