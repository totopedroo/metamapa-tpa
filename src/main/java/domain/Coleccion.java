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
import java.util.Map;
import java.util.HashMap;

public class Coleccion {
    //private List<Hecho> hechos; //verificar nombre
    public String titulo;
    public String descripcion;
    public  criterioDePertenencia criterioDePertenencia;

    public Coleccion(String titulo, String descripcion, criterioDePertenencia criterioDePertenencia) {
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



    public void setCriterioDeDependencia(String criterioDeDependencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }
    public void leerSegunCriterios(List<criterioDePertenencia> criterios, String archivoCSV) {
        try (
                CSVReader reader = new CSVReaderBuilder(new FileReader(archivoCSV))
                        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                        .build()
        ) {
            String[] cabecera = reader.readNext();
            if (cabecera == null) return;

            String[] fila;
            while ((fila = reader.readNext()) != null) {
                Map<String, String> filaComoMapa = new HashMap<>();
                for (int i = 0; i < cabecera.length && i < fila.length; i++) {
                    filaComoMapa.put(normalizar(cabecera[i]), fila[i]);
                }

                boolean cumpleTodos = criterios.stream().allMatch(c -> {
                    String valorEnFila = filaComoMapa.getOrDefault(normalizar(c.columna), "");
                    return c.cumple(valorEnFila);
                });

                if (cumpleTodos) {
                    System.out.println("Fila: " + filaComoMapa);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
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




