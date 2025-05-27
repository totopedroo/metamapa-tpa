package ar.edu.utn.frba.domain;

import ar.edu.utn.frba.Enums.TipoFuente;

public class main {

  public static void main(String[] args) {
   ImportadorCSV importadorCSV = new ImportadorCSV();
   importadorCSV.importar(new Fuente("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv",importadorCSV, TipoFuente.LOCAL));

  }
}






