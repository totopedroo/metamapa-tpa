package domain;

import mocks.CriterioDePertenencia;

import java.time.LocalDate;

public class Visualizador {
    public String nombre;


    public Visualizador(String nombre) {
        this.nombre = nombre;
    }


    public void agregarFiltro(Coleccion coleccion, criterioDePertenencia criterio) {
        coleccion.agregarCriterio(criterio);
    }
}