package ar.edu.utn.frba.Dtos;

import ar.edu.utn.frba.domain.Coleccion;
import lombok.Data;

@Data
public class ColeccionDto {
    private long id;
    private String titulo;
    private String descripcion;
    private int cantidadDeHechos;

    public ColeccionDto(Coleccion coleccion) {
        this.id = coleccion.getId();
        this.titulo = coleccion.getTitulo();
        this.descripcion = coleccion.getDescripcion();
        this.cantidadDeHechos = coleccion.getHechosVisibles().size();
    }
}
