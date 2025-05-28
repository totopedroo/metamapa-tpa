package ar.edu.utn.frba.domain;

import ar.edu.utn.frba.Enums.TipoFuente;

import java.time.LocalDate;

public class main {

  public static void main(String[] args) {
   ImportadorCSV importadorCSV = new ImportadorCSV();
   importadorCSV.importar(new Fuente("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv",importadorCSV, TipoFuente.LOCAL));
    FuenteDinamicaImpl fuenteDinamica = new FuenteDinamicaImpl();
    fuenteDinamica.crearHecho(new Contribuyente("ezequiel"), "Accidente", "Accidente en ruta 2", "Accidente automovilistico", null, 70.00, 70.00, LocalDate.now());
  }
}






