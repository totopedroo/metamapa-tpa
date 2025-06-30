package ar.edu.utn.frba.Fuente_Dinamica.Domain;

import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Importador;
import lombok.Getter;

import java.util.List;

@Getter
public class Fuente {

  private TipoFuente tipo;

  public Fuente( TipoFuente tipo) {
    this.tipo = tipo;
  }
}

