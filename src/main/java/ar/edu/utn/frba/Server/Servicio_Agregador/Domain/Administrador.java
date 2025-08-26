package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import java.util.ArrayList;
import java.util.List;

public class Administrador {

    private String nombre;
    private String email;
    public List<Coleccion> coleccionesCreadas;

    public Administrador(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.coleccionesCreadas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public List<Coleccion> getColeccionesCreadas() {
        return coleccionesCreadas;
    }


}
