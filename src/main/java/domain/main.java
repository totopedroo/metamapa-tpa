package domain;

import domain.Coleccion;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class main {
    //Escenario 1
    public static void main(String[] args) {
        Administrador admin = new Administrador("Juan", "juan@metamapa.org");
  /*
  List<criterioDePertenencia> criterios = new ArrayList<>();
       criterioDePertenencia criterio1 = new criterioDePertenencia("", "");
       criterios.add(criterio1);
       Coleccion coleccion1 = admin.crearColeccion("Coleccion prueba", "esto es una prueba", criterio1);
       coleccion1.leerSegunCriterios(criterios, "prueba1.csv");
        System.out.println("Colección creada: " + coleccion1.getTitulo());
*/
        //Escenario 1.2

        List<criterioDePertenencia> criterios2 = new ArrayList<>();
        criterioDePertenencia criterioFecha1 = new criterioDePertenencia(
                "fecha del hecho",
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2010, 1, 1)
        );
        criterios2.add(criterioFecha1);
        Coleccion coleccion2 = admin.crearColeccion("Hechos 2000-2010", "esta es la segunda prueba", criterioFecha1);
        //coleccion2.leerSegunCriterios(criterios2, "prueba1.csv");
        //System.out.println("Colección creada: " + coleccion2.getTitulo());


        criterioDePertenencia criterioCategoria = new criterioDePertenencia("Categoria", "Caída de aeronave");
        criterios2.add(criterioCategoria);
        System.out.println("Filtrando por fecha y catgria");
        coleccion2.leerSegunCriterios(criterios2, "prueba1.csv");
        // Esperado: El segundo hecho se excluye, porque no es de esa categoría
    }
}







