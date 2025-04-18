package domain;

import domain.Coleccion;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class main {

    public static void main(String[] args) {
        Administrador admin = new Administrador("Juan", "juan@metamapa.org");
  //Escenario 1
  List<criterioDePertenencia> criterios = new ArrayList<>();
       criterioDePertenencia criterio1 = new criterioDePertenencia("", "");
       criterios.add(criterio1);
       Coleccion coleccion1 = admin.crearColeccion("Coleccion prueba", "esto es una prueba", criterios);
       coleccion1.leerSegunCriterios(criterios, "prueba1.csv");
        System.out.println("Colección creada: " + coleccion1.getTitulo());

        //Escenario 1.2

        List<criterioDePertenencia> criterios2 = new ArrayList<>();
        criterioDePertenencia criterioFecha1 = new criterioDePertenencia(
                "fecha del hecho",
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2010, 1, 1)
        );
        criterios2.add(criterioFecha1);
        Coleccion coleccion2 = admin.crearColeccion("HECHOS 2001-2010", "esta es la segunda prueba", criterios2);
        coleccion2.leerSegunCriterios(criterios2, "prueba1.csv");
        System.out.println("CREADA LA COLECCION:  " + coleccion2.getTitulo());


        criterioDePertenencia criterioCategoria = new criterioDePertenencia("Categoria", "Caida de aeronave");
        criterios2.add(criterioCategoria);
        System.out.println("FILTRO POR FECHA Y CATEGORIA");
        coleccion2.leerSegunCriterios(criterios2, "prueba1.csv");
        // Esperado: El segundo hecho se excluye, porque no es de esa categoría


        //Escenario 1.3
        System.out.println("AHORA AGREGO FILTROS DE VISITNANTE, POR LO QUE NO TENDRÍA QUE DAR NINGUN HECHO");
        criterioDePertenencia criterioBautista = new criterioDePertenencia("Categoria", "caída de aeronave");
        criterioDePertenencia criterioBautista2 = new criterioDePertenencia("Titulo", "untitulo");
        Visualizador bautista = new Visualizador("Bautista");
        bautista.agregarFiltro(coleccion2, criterioBautista);
        bautista.agregarFiltro(coleccion2, criterioBautista2);
        coleccion2.leerSegunCriterios(criterios2, "prueba1.csv");

        //Escenario 1.4



        //Escenario 2
        //esto lo podemos hacer de dos formas, o ceando un nuevo metodo en administrador, o usando el metodo de coleccion, pero sin agregar filtros y se crearía una "supercoloeccion"
       admin.leerCSV("archivodefinitivo.csv");



    }
}







