package ar.edu.utn.frba.Server.Fuente_Dinamica.Domain;

import ar.edu.utn.frba.Server.Enums.TipoFuente;
import lombok.Getter;

@Getter
public class Fuente {

  private TipoFuente tipo;

  public Fuente( TipoFuente tipo) {
    this.tipo = tipo;
  }
}

