package ar.edu.utn.frba.Servicio_Agregador.Dtos;


import ar.edu.utn.frba.Servicio_Agregador.Domain.CriterioDePertenencia;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDto {
    //public String id; //creo que no tiene que ser input
    private List<Hecho> hechos; //verificar nombre
    public String titulo;
    public String descripcion;
    public  List<CriterioDePertenencia> criterioDePertenencia;
}