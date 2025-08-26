package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import java.util.List;

public interface Importador {
  List<Hecho> importar(Fuente fuente);
}

