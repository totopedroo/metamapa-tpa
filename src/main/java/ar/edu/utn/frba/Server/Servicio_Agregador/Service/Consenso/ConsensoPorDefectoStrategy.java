package ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso;


import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto;

import java.util.List;
import java.util.Optional;


public class ConsensoPorDefectoStrategy implements AlgoritmoDeConsensoStrategy {
    @Override
    public void procesarYEstablecerConsenso(Hecho hechoAProcesar, List<Hecho> todosLosHechos) {
        // Por defecto, siempre hay consenso.
        hechoAProcesar.setConsensuado(Optional.of(true));
    }
}