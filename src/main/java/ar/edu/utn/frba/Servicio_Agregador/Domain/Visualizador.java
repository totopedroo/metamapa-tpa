package ar.edu.utn.frba.Servicio_Agregador.Domain;

import ar.edu.utn.frba.domain.CriterioDePertenencia;

import java.util.ArrayList;
import java.util.List;

public class Visualizador {
    public String nombre;
    public List<ar.edu.utn.frba.domain.CriterioDePertenencia> filtrosPersonales = new ArrayList<>();

    public Visualizador(String nombre) {
        this.nombre = nombre;
    }


    public void agregarFiltro(CriterioDePertenencia criterio) {
        filtrosPersonales.add(criterio);
    }

}