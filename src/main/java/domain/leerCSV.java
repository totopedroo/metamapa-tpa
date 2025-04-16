package ar.edu.utn.frba;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;
import java.text.Normalizer;

public class leerCSV {

    public static void leerConFiltro(String archivo, String columna, String valorBuscado) {
        try (
                CSVReader reader = new CSVReaderBuilder(new FileReader(archivo))
                        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                        .build() //divide las columnas usando el separador ;, si usaramos otro sed tomaria solo como una columna ya que en el csv se usa ";"
        ) {
            String[] cabecera = reader.readNext();
            if (cabecera == null) {
                System.out.println("El archivo está vacío.");
                return;
            }//lee la cabecera, en la que estan las columnas

            // busco la columna normalizando las tildes y mayusculas
            int indice = -1;
            for (int i = 0; i < cabecera.length; i++) {
                if (normalizar(cabecera[i]).equalsIgnoreCase(normalizar(columna))) {
                    indice = i;
                    break;
                }
            }

            if (indice == -1) {
                System.out.println("No se encontró la columna: " + columna);
                return;
            }

            // lee y filtra las columnas con el filtro que le dimos (Stringcolumna y valorBuscado
            String[] fila;
            while ((fila = reader.readNext()) != null) {
                if (fila.length > indice && fila[indice].equalsIgnoreCase(valorBuscado)) {
                    System.out.println(Arrays.toString(fila));
                }
            }

            /*for(int j = 0; j< fila.length ; j++){
                if (fila[j] == fila[j+1]){
                        fila[j+1] = "";
                }
            }
*/
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        }
    }

    private static String normalizar(String texto) { //por los datos que utilizo, me encargue de sacar todas las tildes, sin embargo por las dudas agrego el normalizador
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // quita tildes y //p... es una clase especial de caracteres Unicode que representa todos los acentos, tildes, etc...
                .toLowerCase()
                .trim(); //removemos los espacios blancos
    }

    public static void main(String[] args) {
        leerConFiltro("archivodefinitivo.csv", "Categoria", "Accidente automovilistico");
    }

}
