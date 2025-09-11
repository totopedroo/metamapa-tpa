package ar.edu.utn.frba.server.servicioAgregador.domain.navegacion;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;

import java.util.List;

public interface ModoNavegacionStrategy {
    List<Hecho> filtrar(List<Hecho> hechos);
}
