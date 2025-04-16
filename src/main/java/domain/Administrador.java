package domain;

import mocks.Coleccion;
import mocks.Fuente;
import mocks.CriterioDePertenencia;
import mocks.SolicitudEliminacion;

import java.util.ArrayList;
import java.util.List;

public class Administrador {

    private String nombre;
    private String email;
    private List<Coleccion> coleccionesCreadas;

    public Administrador(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.coleccionesCreadas = new ArrayList<>();
    }

    public Coleccion crearColeccion(String titulo, String descripcion, Fuente fuente, CriterioDePertenencia criterio) {
        Coleccion coleccion = new Coleccion(titulo, descripcion, fuente, criterio);
        coleccionesCreadas.add(coleccion);
        return coleccion;
    }

    public void importarHechosDesdeCSV(String rutaArchivoCSV, Fuente fuente) {
        // Se delega la lectura a Fuente (otros compañeros se encargan)
        fuente.importarHechosDesdeCSV(rutaArchivoCSV);
    }

    public void aceptarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        solicitud.aceptar();
    }

    public void rechazarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        solicitud.rechazar();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Coleccion> getColeccionesCreadas() {
        return coleccionesCreadas;
    }
}