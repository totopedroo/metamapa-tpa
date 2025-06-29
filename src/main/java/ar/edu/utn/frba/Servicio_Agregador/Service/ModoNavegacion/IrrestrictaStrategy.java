package ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;

import java.util.List;


    public class IrrestrictaStrategy implements ModoNavegacionStrategy {


        @Override
        public List<Hecho> filtrar(List<Hecho> hechos) {
            return hechos;
        }
}
