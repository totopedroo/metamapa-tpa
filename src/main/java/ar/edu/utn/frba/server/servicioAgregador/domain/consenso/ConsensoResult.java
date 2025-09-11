package ar.edu.utn.frba.server.servicioAgregador.domain.consenso;

import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import lombok.Value;
import lombok.Builder;

import java.util.List;

/**
 * Resultado “rico” de un algoritmo de consenso para un Hecho.
 * Inmutable: compara por valor, seguro para cache/test.
 */

@Value
@Builder
public class ConsensoResult {
    EstadoConsenso estado;          // CONSENSUADO | CONFLICTO | INSUFICIENTE | NO_CONSENSUADO
    int soportes;                   // # de fuentes que coinciden
    int totalFuentes;               // # de fuentes consideradas
    List<TipoFuente> fuentesSoporte;
    List<String> conflictos;        // ids/títulos/razones en conflicto (si aplica)
}
