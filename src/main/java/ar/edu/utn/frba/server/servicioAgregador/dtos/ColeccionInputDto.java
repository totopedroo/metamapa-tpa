package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.common.enums.TipoAlgoritmoConsenso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionInputDto {

    private String titulo;
    private String descripcion;

    private List<Long> idsHechos;
    private List<CriterioDePertenenciaDto> criterioDePertenencia;
    private TipoAlgoritmoConsenso algoritmo;
    private boolean eliminada = false;
}
