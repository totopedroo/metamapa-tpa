package ar.edu.utn.frba.Server.Servicio_Agregador.Dtos;


import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.CriterioDePertenencia;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColeccionOutputDto {
    private String id;
    private String titulo;
    private String descripcion;
    private List<HechosOutputDto> hechos = new ArrayList<>();


    // Exponemos sólo nombres/etiquetas, no objetos de estrategia
    private List<CriterioDePertenencia> criterios; // nombres simples de criterios
    private AlgoritmoDeConsensoStrategy algoritmoDeConsenso; // nombre del algoritmo


    public static ColeccionOutputDto fromModel(Coleccion c) {
        ColeccionOutputDto dto = new ColeccionOutputDto();
        if (c == null) return dto;


        dto.setId(c.getId());
        dto.setTitulo(c.getTitulo());
        dto.setDescripcion(c.getDescripcion());


        dto.setHechos(
                c.getHechos() == null ? List.of() :
                        c.getHechos().stream().map(HechosOutputDto::fromModel).collect(Collectors.toList())
        );


        dto.setCriterios(
                c.getCriterioDePertenencia() == null ? List.of()
                        : c.getCriterioDePertenencia()
        );

        dto.setAlgoritmoDeConsenso(c.getAlgoritmoDeConsenso());

        return dto;
    }
}

