package domain;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import java.text.Normalizer;
import java.util.Map;
import java.util.HashMap;

public class Coleccion {
    private List<Hecho> hechos; //verificar nombre
    public String titulo;
    public String descripcion;
    public  List<CriterioDePertenencia> criterioDePertenencia;

    public Coleccion(String titulo, String descripcion, List<CriterioDePertenencia> criterioDePertenencia) {
        this.hechos = new ArrayList<>();
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

    public List<Hecho> getHechos() {
        return hechos;
    }

    public List<Hecho> getHechosVisibles() {
        return hechos.stream()
            .filter(h -> !h.eliminado)
            .toList();
    }

    public void setHechos(List<Hecho> hechos) {
        this.hechos = hechos;
    }

    public List<CriterioDePertenencia> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }

    public void setCriterioDePertenencia(List<CriterioDePertenencia> criterioDePertenencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

    public void setCriterioDeDependencia(String criterioDeDependencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

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

                    // Construir Hecho (adaptalo según tu constructor real)
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



    public void agregarCriterio(CriterioDePertenencia criterio) {
        criterioDePertenencia.add(criterio);
    }


    public  String normalizar(String texto) { //por los datos que utilizo, me encargue de sacar todas las tildes, sin embargo por las dudas agrego el normalizador
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // quita tildes y //p... es una clase especial de caracteres Unicode que representa todos los acentos, tildes, etc...
            .toLowerCase()
            .trim(); //removemos los espacios blancos
    }

    public void agregarHecho(Hecho hecho) {
        if (hecho.estaEliminado()) {
            System.out.println("No se puede agregar el hecho '" + hecho.titulo + "' porque fue eliminado.");
            return;
        }

        this.hechos.add(hecho);
        System.out.println("Hecho agregado a la colección: " + hecho.titulo);
    }


}



