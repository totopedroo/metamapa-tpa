package domain;

import java.time.LocalDate;

public class filtroCSV {

   public String archivo;
   public String columna;
   public String criterioDePertenencia;
   public Coleccion coleccion;
   public String origen;
   public String fechaCarga; //tinee que ser LocalDATE

    public filtroCSV(String columna, String criterioDePertenencia, Coleccion coleccion, String archivo, String origen, LocalDate fechaCarga) {
        this.archivo = archivo;
        this.columna = columna;
        this.criterioDePertenencia = criterioDePertenencia;
        this.coleccion = coleccion;
        this.origen = origen;
        this.fechaCarga = fechaCarga.toString();

    }

}
