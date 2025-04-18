package domain;

import java.time.LocalDate;
import java.util.List;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import java.text.Normalizer;


public class Coleccion {
    //private List<Hecho> hechos; //verificar nombre
    public String titulo;
    public String descripcion;
    public String criterioDePertenencia;

    public Coleccion(String titulo, String descripcion, String criterioDePertenencia) {
        //  this.hechos = new ArrayList<>();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterioDePertenencia = criterioDePertenencia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String nombre) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getcriterioDePertenencia() {
        return criterioDePertenencia;
    }

    public void setCriterioDeDependencia(String criterioDeDependencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

   public  void leerSegunCriterio(filtroCSV filtro) {//un solo parametro por resolver code smell de parametros largos
        try (
                CSVReader reader = new CSVReaderBuilder(new FileReader(filtro.archivo))
                        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                        .build() //divide las columnas usando el separador ;, si usaramos otro sed tomaria solo como una columna ya que en el csv se usa ";"
        ) {
            System.out.println(filtro.coleccion.titulo);
            System.out.println(filtro.coleccion.descripcion);
            String[] cabecera = reader.readNext();
            if (cabecera == null) {
                System.out.println("El archivo está vacío.");
                return;
            }
            //lee la cabecera, en la que estan las columnas
            // busco la columna normalizando las tildes y mayusculas
            int indice = -1;
            for (int i = 0; i < cabecera.length; i++) {
                if (normalizar(cabecera[i]).equalsIgnoreCase(normalizar(filtro.columna))) {
                    indice = i;
                    break;
                }
            }
            if (indice == -1) {
                System.out.println("No se encontró la columna: " + filtro.columna);
                return;
            }
            // lee y filtra las columnas con el filtro que le dimos (Stringcolumna y criterioDePertenencia
            String[] columna;
            while ((columna = reader.readNext()) != null) {
                if (columna.length > indice && columna[indice].equalsIgnoreCase(criterioDePertenencia)) {
                    String[] filaExtendida = Arrays.copyOf(columna, columna.length + 2);
                    filaExtendida[columna.length] = filtro.origen;
                    filaExtendida[columna.length + 1] = filtro.fechaCarga;

                    System.out.println(Arrays.toString(filaExtendida));
                    ;
                }
            }
        } catch (IOException e) {
            System.err.println("eror no se pudo leer el archivo");
        }
    }

    public  String normalizar(String texto) { //por los datos que utilizo, me encargue de sacar todas las tildes, sin embargo por las dudas agrego el normalizador
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // quita tildes y //p... es una clase especial de caracteres Unicode que representa todos los acentos, tildes, etc...
                .toLowerCase()
                .trim(); //removemos los espacios blancos
    }

}
    //prueba
   /* public static void main(String[] args) {
    Coleccion coleccion1 = new Coleccion("Desastres Naturales 2000-2025", "Desastres naturales de Argentina en los ultimos años", "Natural");
    filtroCSV filtroParaColeccion1 = new filtroCSV("Categoria", coleccion1.criterioDePertenencia,coleccion1, "archivodefinitivo.csv", "Manual", LocalDate.now());
    coleccion1.leerSegunCriterio(filtroParaColeccion1);


    }
*/




