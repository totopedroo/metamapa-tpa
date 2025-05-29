package ar.edu.utn.frba.Dtos;

import ar.edu.utn.frba.domain.Coleccion;
import ar.edu.utn.frba.domain.CriterioDePertenencia;
import ar.edu.utn.frba.domain.Hecho;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ColeccionOutputDto {
    public String id;
    private List<Hecho> hechos; //verificar nombre
    public String titulo;
    public String descripcion;
    public  List<CriterioDePertenencia> criterioDePertenencia;




    public static ColeccionOutputDto fromModel(Coleccion coleccion) {
        ColeccionOutputDto dto = new ColeccionOutputDto();
        dto.id = coleccion.getId();
        dto.hechos = new ArrayList<Hecho>();
        dto.titulo = coleccion.getTitulo();
        dto.descripcion = coleccion.getDescripcion();
        dto.criterioDePertenencia = coleccion.getCriterioDePertenencia();
        return dto;
    }
}