package ar.edu.utn.frba;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;


public class leerCSV {
    public static void main(String[] args) {
        String accidentes = "accidentes.csv";

        try (CSVReader reader = new CSVReader(new FileReader(accidentes))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                for (String valor : linea) {
                    System.out.print(valor + " | ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace(); //excepcion de error
        }
    }
}