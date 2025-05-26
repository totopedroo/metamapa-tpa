package ar.edu.utn.frba.domain;
import java.util.List;
import java.util.ArrayList;
public class ColeccionFactory {

    public static Coleccion crear(String tipo, String id, String titulo, String descripcion, List<CriterioDePertenencia> criterios) {
        return switch (tipo) {
            case "default" -> new Coleccion(id, titulo, descripcion, criterios);
            default -> throw new RuntimeException("Tipo no implementado: " + tipo);
        };
    }
}
