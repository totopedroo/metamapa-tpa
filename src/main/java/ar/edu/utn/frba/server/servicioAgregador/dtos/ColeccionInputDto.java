package ar.edu.utn.frba.server.servicioAgregador.dtos;


import ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AlgoritmoDeConsensoStrategy;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDto {
    //public String id;
    private List<Long> idsHechos;
    public String titulo;
    public String descripcion;
    public  List<CriterioDePertenenciaDto> criterioDePertenencia;
    private TipoAlgoritmoConsenso algoritmo;
}