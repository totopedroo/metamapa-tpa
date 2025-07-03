package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;


import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;

import java.util.List;


public class ConsensoPorDefectoStrategy implements AlgoritmoDeConsensoStrategy {
    @Override
    public boolean tieneConsenso(Hecho hecho, List<Hecho> todos) {
        return true;
    }
}