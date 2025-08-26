package ar.edu.utn.frba.Server.Fuente_Proxy.Domain;

import java.util.List;

public interface Importador {
  List<Hecho> importar(Fuente fuente);
}

