package ar.edu.utn.frba.Servicio_Agregador.Dtos;


import ar.edu.utn.frba.Servicio_Agregador.Domain.CriterioDePertenencia;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDto {
    //public String id;
    private List<Hecho> hechos;
    public String titulo;
    public String descripcion;
    public  List<CriterioDePertenencia> criterioDePertenencia;
    private AlgoritmoDeConsensoStrategy algoritmoDeConsenso;
}