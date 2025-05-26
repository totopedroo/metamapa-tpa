package ar.edu.utn.frba.Dtos;


import ar.edu.utn.frba.domain.CriterioDePertenencia;
import ar.edu.utn.frba.domain.Hecho;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Data
public class ColeccionInputDto {
    //public String id; //creo que no tiene que ser input
    private List<Hecho> hechos; //verificar nombre
    public String titulo;
    public String descripcion;
    public  List<CriterioDePertenencia> criterioDePertenencia;
}