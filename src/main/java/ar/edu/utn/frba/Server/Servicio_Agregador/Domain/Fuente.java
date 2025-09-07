package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import ar.edu.utn.frba.Server.Enums.TipoFuente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Fuente")
public class Fuente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String path;
  @Transient
  private Importador importador;
  @Column(name="tipo_fuente")
  @Enumerated(EnumType.STRING)
  private TipoFuente tipo;


  public Fuente(String url, ImportadorAPI importadorAPI, ar.edu.utn.frba.Server.Servicio_Agregador.Domain.TipoFuente tipoFuente) {
  }

  //public boolean esFuenteProxy() { return tipo == TipoFuente.PROXY;}

  public List<Hecho> obtenerHechos() {
    return importador.importar(this);
  }

}

