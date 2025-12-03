package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoConsensoDto {
    private String algoritmo;          // "mayoriaSimple", "multiplesMenciones", "absoluta", "defecto"
    private EstadoConsenso estado;     // CONSENSUADO | CONFLICTO | INSUFICIENTE | NO_CONSENSUADO
    private int soportes;
    private int totalFuentes;
}

