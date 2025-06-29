package ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;

import java.util.List;

public interface ModoNavegacionStrategy {
    List<Hecho> filtrar(List<Hecho> hechos);
}
