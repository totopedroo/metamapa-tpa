package ar.edu.utn.frba.Servicio_Agregador.Dtos;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Servicio_Agregador.Domain.CriterioDePertenencia;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
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
    private AlgoritmoDeConsensoStrategy algoritmoDeConsenso;


    public static ColeccionOutputDto fromModel(Coleccion coleccion) {
        ColeccionOutputDto dto = new ColeccionOutputDto();
        dto.id = coleccion.getId();
        dto.hechos = new ArrayList<Hecho>();
        dto.titulo = coleccion.getTitulo();
        dto.descripcion = coleccion.getDescripcion();
        dto.criterioDePertenencia = coleccion.getCriterioDePertenencia();
        dto.algoritmoDeConsenso = coleccion.getAlgoritmoDeConsenso();
        return dto;
    }
}