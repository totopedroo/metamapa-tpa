package ar.edu.utn.frba.Fuente_Proxy.Domain;

import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.Importador;
import lombok.Getter;

import java.util.List;

@Getter
public class Fuente {
  private String path;
  private ar.edu.utn.frba.domain.Importador importador;
  private TipoFuente tipo;

  public Fuente(String path, Importador importador, TipoFuente tipo) {
    this.path = path;
    this.importador = importador;
    this.tipo = tipo;
  }

  //public boolean esFuenteProxy() { return tipo == TipoFuente.PROXY;}

  public List<Hecho> obtenerHechos() {
    return importador.importar(this);
  }

}

