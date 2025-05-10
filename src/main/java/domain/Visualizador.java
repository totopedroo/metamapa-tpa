package domain;

import java.util.ArrayList;
import java.util.List;

public class Visualizador {
    public String nombre;
    public List<CriterioDePertenencia> filtrosPersonales = new ArrayList<>();

    public Visualizador(String nombre) {
        this.nombre = nombre;
    }

    public void visualizarHechos(Coleccion coleccion) {
        System.out.println("TODOS los hechos disponibles en la colección '" + coleccion.getTitulo() + "' para " + nombre + ":");
        for (Hecho hecho : coleccion.getHechos()) {
            System.out.println("-> " + hecho.getTitulo());
        }
    }

    public void agregarFiltro(CriterioDePertenencia criterio) {
        filtrosPersonales.add(criterio);
    }

    public void visualizarHechosFiltrados(Coleccion coleccion) {
        System.out.println("Hechos visibles para " + nombre + " en colección '" + coleccion.getTitulo() + "':");
        for (Hecho hecho : coleccion.getHechos()) {
            boolean cumple = filtrosPersonales.stream().allMatch(c -> c.cumple(hecho));
            if (cumple) {
                System.out.println("-> " + hecho.getTitulo());
            }
        }
    }
}