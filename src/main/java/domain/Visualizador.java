package domain;

public class Visualizador {
    public String nombre;


    public Visualizador(String nombre) {
        this.nombre = nombre;
    }


    public void agregarFiltro(Coleccion coleccion, CriterioDePertenencia criterio) {
        coleccion.agregarCriterio(criterio);
    }
}