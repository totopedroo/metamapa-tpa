package ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Component("curada")
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



