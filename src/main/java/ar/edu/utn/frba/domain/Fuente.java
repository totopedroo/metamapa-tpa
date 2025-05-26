package ar.edu.utn.frba.domain;

import java.util.List;

public class Fuente {
  private String path;
  private Importador importador;

  public Fuente(String path, Importador importador) {
    this.path = path;
    this.importador = importador;
  }

  public List<Hecho> obtenerHechos() {
    return importador.importar(this);
  }

  public String getPath() {
    return path;
  }
}

