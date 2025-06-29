package ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CuradaStrategy implements ModoNavegacionStrategy {

    private final AlgoritmoDeConsensoStrategy algoritmoDeConsenso;

    public CuradaStrategy(AlgoritmoDeConsensoStrategy algoritmoDeConsenso) {
        this.algoritmoDeConsenso = algoritmoDeConsenso;
    }


    @Override
    public List<Hecho> filtrar(List<Hecho> hechos) {
        if (hechos == null || hechos.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Object, List<Hecho>> hechosAgrupadosPorFuente = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getFuente));

        List<List<Hecho>> hechosPorFuente = new ArrayList<>(hechosAgrupadosPorFuente.values());

        return this.algoritmoDeConsenso.obtenerHechosConsensuados(hechosPorFuente);
    }
}