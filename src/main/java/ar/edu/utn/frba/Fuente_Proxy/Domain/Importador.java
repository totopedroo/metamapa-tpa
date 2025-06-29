package ar.edu.utn.frba.Fuente_Proxy.Domain;

import ar.edu.utn.frba.Fuente_Proxy.Domain.Fuente;
import ar.edu.utn.frba.Fuente_Proxy.Domain.Hecho;

import java.util.List;

public interface Importador {
  List<Hecho> importar(Fuente fuente);
}

