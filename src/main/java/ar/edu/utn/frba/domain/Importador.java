package ar.edu.utn.frba.domain;

import java.util.List;

public interface Importador {
  List<Hecho> importar(Fuente fuente);
}

