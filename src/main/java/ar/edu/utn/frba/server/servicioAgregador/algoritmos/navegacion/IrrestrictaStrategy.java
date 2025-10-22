package ar.edu.utn.frba.server.servicioAgregador.algoritmos.navegacion;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component("irrestricta")
@Service
public class IrrestrictaStrategy implements ModoNavegacionStrategy {
        @Override
        public List<Hecho> filtrar(List<Hecho> hechos) {
            return hechos;
        }
    }