package ar.edu.utn.frba.server.servicioAgregador.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionFrontDto {
    private Long id;
    private String titulo;
    private String descripcion;
    private List<HechoFrontDto> hechos;   // Hechos homogéneos
    private List<CriterioDePertenenciaDto> criterios;
    private String algoritmoDeConsenso;
    private String fuente;
}

