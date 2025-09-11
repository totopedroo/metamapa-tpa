package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColeccionOutputDto {
    private String id;
    private String titulo;
    private String descripcion;

    private List<HechosOutputDto> hechos;

    // 👇 Ahora DTOs, no objetos de dominio
    private List<CriterioDePertenenciaDto> criterios;

    // 👇 Las estrategias siguen siendo problemáticas -> exponer nombre simple
    private String algoritmoDeConsenso;

    public static ColeccionOutputDto fromModel(Coleccion c) {
        ColeccionOutputDto dto = new ColeccionOutputDto();
        if (c == null) return dto;

        dto.setId(c.getId());
        dto.setTitulo(c.getTitulo());
        dto.setDescripcion(c.getDescripcion());

        dto.setHechos(
                c.getHechos() == null ? List.of()
                        : c.getHechos().stream().map(HechosOutputDto::fromModel).toList()
        );

        dto.setCriterios(
                c.getCriterioDePertenencia() == null ? List.of()
                        : c.getCriterioDePertenencia().stream()
                        .map(CriterioDePertenenciaDto::fromModel)
                        .toList()
        );

        dto.setAlgoritmoDeConsenso(
                c.getAlgoritmoDeConsenso() == null ? null
                        : c.getAlgoritmoDeConsenso().getClass().getSimpleName()
        );

        return dto;
    }

    // ✅ Adaptador para que compile tu línea vieja:
    // dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
    public void setCriterioDePertenencia(List<CriterioDePertenencia> cps) {
        this.criterios = (cps == null ? List.of()
                : cps.stream().map(CriterioDePertenenciaDto::fromModel).collect(Collectors.toList()));
    }
}
