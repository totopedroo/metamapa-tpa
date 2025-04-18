package domain;


import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Administrador admin = new Administrador("Juan", "juan@metamapa.org");
        Coleccion coleccion1 = admin.crearColeccion("Incendios en 2025", "Todos los incendios en arg", "Incendio");
        filtroCSV filtroParaColeccion1 = new filtroCSV("Categoria", coleccion1.criterioDePertenencia, coleccion1, "archivodefinitivo.csv", "Manual", LocalDate.now());
        coleccion1.leerSegunCriterio(filtroParaColeccion1);

        System.out.println("Colección creada: " + coleccion1.getTitulo());



       // SolicitudEliminacion solicitud = new SolicitudEliminacion("Este hecho contiene datos sensibles...");
        //admin.aceptarSolicitudEliminacion(solicitud);

    }
}
