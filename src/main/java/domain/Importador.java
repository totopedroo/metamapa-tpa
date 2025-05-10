package domain;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
public class Importador {
    public List<Hecho> hechos = new ArrayList();
    public List<Hecho> getHechos() {
        return hechos;
    }

    public void importarFromCSV( String archivoCSV) {
        try (
                CSVReader reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(archivoCSV), "ISO-8859-1"))
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
                    System.out.println("Fila válida: " + filaComoMapa);


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                    Hecho hecho = new Hecho(
                            filaComoMapa.get("titulo"),
                            filaComoMapa.get("descripcion"),
                            filaComoMapa.get("categoria"),
                            null,
                            Double.parseDouble(filaComoMapa.get("latitud").replace(",", ".")),
                            Double.parseDouble(filaComoMapa.get("longitud").replace(",", ".")),
                            LocalDate.parse(filaComoMapa.get("fecha del hecho"), formatter),
                            LocalDate.now()
                    );

                   this.hechos.add(hecho);

            }

        } catch (IOException e) {
            System.err.println("ERROR al leer el CSV: " + e.getMessage());
        }
    }
    public  String normalizar(String texto) { //por los datos que utilizo, me encargue de sacar todas las tildes, sin embargo por las dudas agrego el normalizador
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // quita tildes y //p... es una clase especial de caracteres Unicode que representa todos los acentos, tildes, etc...
                .toLowerCase()
                .trim(); //removemos los espacios blancos
    }
    public void visualizarHechos(){//Coleccion coleccion) {
        System.out.println("TODOS los hechos disponibles en la colección '" +  "' para prueba "  + ":");
        for (Hecho hecho : this.getHechos()) {
            System.out.println("-> " + hecho.getCategoria());
        }
    }
    /*
    public void leerSegunCriterios(List<CriterioDePertenencia> criterios, String archivoCSV) {
        try (
                CSVReader reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(archivoCSV), "ISO-8859-1"))
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
                    String valorEnFila = filaComoMapa.getOrDefault(normalizar(c.getColumna()), "VACIO");
                    return c.cumple(valorEnFila);
                });

                if (cumpleTodos) {
                    System.out.println("Fila válida: " + filaComoMapa);


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                    Hecho hecho = new Hecho(
                            filaComoMapa.get("titulo"),
                            filaComoMapa.get("descripcion"),
                            filaComoMapa.get("categoria"),
                            null,
                            Double.parseDouble(filaComoMapa.get("latitud").replace(",", ".")),
                            Double.parseDouble(filaComoMapa.get("longitud").replace(",", ".")),
                            LocalDate.parse(filaComoMapa.get("fecha del hecho"), formatter),
                            LocalDate.now()
                    );

                    this.hechos.add(hecho);
                }
            }

        } catch (IOException e) {
            System.err.println("ERROR al leer el CSV: " + e.getMessage());
        }
    }
    */

}
