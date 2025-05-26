package ar.edu.utn.frba.domain;

import ar.edu.utn.frba.Enums.TipoFuente;
import java.util.List;

public class Fuente {
  private String path;
  private Importador importador;
  private TipoFuente tipo;

  public Fuente(String path, Importador importador, TipoFuente tipo) {
    this.path = path;
    this.importador = importador;
    this.tipo = tipo;
  }

  public boolean esFuenteProxy() { return tipo == TipoFuente.PROXY;}

  public List<Hecho> obtenerHechos() {
    return importador.importar(this);
  }

  public String getPath() {
    return path;
  }
}

