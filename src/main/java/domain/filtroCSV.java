package domain;

public class filtroCSV {

   public String archivo;
   public String columna;
   public String criterioDePertenencia;
   public Coleccion coleccion;

    public filtroCSV(String columna, String criterioDePertenencia, Coleccion coleccion, String archivo) {
        this.archivo = archivo;
        this.columna = columna;
        this.criterioDePertenencia = criterioDePertenencia;
        this.coleccion = coleccion;

    }

}
