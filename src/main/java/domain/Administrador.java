package domain;

import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
public class Administrador {

    private String nombre;
    private String email;
    public List<Coleccion> coleccionesCreadas;

    public Administrador(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.coleccionesCreadas = new ArrayList<>();
    }

    public Coleccion crearColeccion(String titulo, String descripcion, List<CriterioDePertenencia> criterios) {
        Coleccion coleccion = new Coleccion(titulo, descripcion, criterios);
        coleccionesCreadas.add(coleccion);
        return coleccion;
    }

    public void leerCSV(String rutaArchivo) {
        try (
            CSVReader reader = new CSVReaderBuilder(new FileReader(rutaArchivo))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()
        ) {
            String[] fila;
            System.out.println("LEYENDO EL ARCHIVO: " + rutaArchivo);
            while ((fila = reader.readNext()) != null) {
                //System.out.println(Arrays.toString(fila));
            }

        } catch (IOException e) {
            System.err.println("ERROR ");
        }
    }

    public void aceptarSolicitudDeEliminacion(Hecho hecho, SolicitudDeEliminacion solicitud) {
        solicitud.aceptarSolicitud();
        hecho.verificarEliminacion();
    }

    public void rechazarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud) {
        solicitud.rechazarSolicitud();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Coleccion> getColeccionesCreadas() {
        return coleccionesCreadas;
    }


}
