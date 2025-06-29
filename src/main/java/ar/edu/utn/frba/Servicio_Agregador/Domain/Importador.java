package ar.edu.utn.frba.Servicio_Agregador.Domain;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;

import java.util.List;

public interface Importador {
  List<Hecho> importar(Fuente fuente);
}

