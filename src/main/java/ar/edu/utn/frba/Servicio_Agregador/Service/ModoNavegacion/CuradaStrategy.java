package ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CuradaStrategy implements ModoNavegacionStrategy {
    @Override
    public List<Hecho> filtrar(List<Hecho> hechos) {
        List<Hecho> consensuados = new ArrayList<>();

        for (Hecho hecho : hechos) {
            if (hecho.getConsensuado().isPresent() && hecho.getConsensuado().get()) {
                consensuados.add(hecho);
            }
        }

        return consensuados;
    }
}



