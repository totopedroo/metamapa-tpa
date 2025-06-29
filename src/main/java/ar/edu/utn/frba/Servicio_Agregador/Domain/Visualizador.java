package ar.edu.utn.frba.Servicio_Agregador.Domain;

import ar.edu.utn.frba.Servicio_Agregador.Domain.CriterioDePertenencia;

import java.util.ArrayList;
import java.util.List;

public class Visualizador {
    public String nombre;
    public List<CriterioDePertenencia> filtrosPersonales = new ArrayList<>();

    public Visualizador(String nombre) {
        this.nombre = nombre;
    }


    public void agregarFiltro(CriterioDePertenencia criterio) {
        filtrosPersonales.add(criterio);
    }

}