package domain;

import mocks.Coleccion;
import mocks.Fuente;
import mocks.CriterioDePertenencia;
import mocks.CriterioPorCategoria;
import mocks.SolicitudEliminacion;

public class main {
    public static void main(String[] args) {
        Administrador admin = new Administrador("Juan", "juan@metamapa.org");

        Fuente fuente = new Fuente();
        CriterioDePertenencia criterio = new CriterioPorCategoria("Incendio");

        Coleccion coleccion = admin.crearColeccion("Incendios 2025", "Todos los incendios reportados", fuente, criterio);
        System.out.println("Colección creada: " + coleccion.getTitulo());

        admin.importarHechosDesdeCSV("hechos_incendios.csv", fuente);

        SolicitudEliminacion solicitud = new SolicitudEliminacion("Este hecho contiene datos sensibles...");
        admin.aceptarSolicitudEliminacion(solicitud);
    }
}