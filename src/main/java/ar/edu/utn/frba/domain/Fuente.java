package ar.edu.utn.frba.domain;

import java.util.List;

public class Fuente {
  private String path;
  private Importador importador;
  private boolean esProxy;

  public Fuente(String path, Importador importador, boolean esProxy) {
    this.path = path;
    this.importador = importador;
    this.esProxy = esProxy;
  }

  public boolean esFuenteProxy() { return esProxy;}

  public List<Hecho> obtenerHechos() {
    return importador.importar(this);
  }

  public String getPath() {
    return path;
  }
}

