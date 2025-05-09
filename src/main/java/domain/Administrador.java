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

    /*public void aceptarSolicitudDeEliminacion(Hecho hecho, SolicitudDeEliminacion solicitud) {
        solicitud.aceptarSolicitud();
        hecho.verificarEliminacion();
    }

    public void rechazarSolicitudDeEliminacion(SolicitudDeEliminacion solicitud) {
        solicitud.rechazarSolicitud();
    }
*/
    public String getNombre() {
        return nombre;
    }

    public List<Coleccion> getColeccionesCreadas() {
        return coleccionesCreadas;
    }


}
