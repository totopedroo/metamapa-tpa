package ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IrrestrictaStrategy implements ModoNavegacionStrategy {
        @Override
        public List<Hecho> filtrar(List<Hecho> hechos) {
            return hechos;
        }
    }