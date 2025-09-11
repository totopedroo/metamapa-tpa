package ar.edu.utn.frba.server.servicioAgregador.domain.consenso;

import ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsensoService {

    private final Map<TipoAlgoritmoConsenso, AlgoritmoDeConsensoStrategy> estrategiasPorEnum;
    private final Map<String, AlgoritmoDeConsensoStrategy> estrategiasPorCodigo;

    public ConsensoService(List<AlgoritmoDeConsensoStrategy> estrategias) {
        this.estrategiasPorEnum  = new EnumMap<>(TipoAlgoritmoConsenso.class);
        this.estrategiasPorCodigo = new HashMap<>();
        for (AlgoritmoDeConsensoStrategy e : estrategias) {
            var tipo = TipoAlgoritmoConsenso.fromCodigo(e.codigo()); // mapea por código ("mayoriaSimple", etc.)
            estrategiasPorEnum.put(tipo, e);
            estrategiasPorCodigo.put(e.codigo(), e);
        }
    }

    /** Evalúa 1..N algoritmos sobre un hecho y devuelve un mapa Algoritmo → Resultado. */
    public Map<TipoAlgoritmoConsenso, ConsensoResult> evaluarHecho(
            Hecho h, List<Hecho> universo, Set<TipoAlgoritmoConsenso> algoritmos) {

        if (algoritmos == null || algoritmos.isEmpty()) return Collections.emptyMap();

        Map<TipoAlgoritmoConsenso, ConsensoResult> out = new EnumMap<>(TipoAlgoritmoConsenso.class);
        for (TipoAlgoritmoConsenso a : algoritmos) {
            AlgoritmoDeConsensoStrategy strat = estrategiasPorEnum.get(a);
            if (strat == null) {
                throw new IllegalArgumentException("Algoritmo no registrado: " + a);
            }
            out.put(a, strat.evaluar(h, universo));
        }
        return out;
    }

    /** Conveniencia: evalúa un solo algoritmo. */
    public ConsensoResult evaluarHecho(Hecho h, List<Hecho> universo, TipoAlgoritmoConsenso algoritmo) {
        AlgoritmoDeConsensoStrategy strat = estrategiasPorEnum.get(algoritmo);
        if (strat == null) throw new IllegalArgumentException("Algoritmo no registrado: " + algoritmo);
        return strat.evaluar(h, universo);
    }

    /** Evalúa todos los algoritmos registrados. */
    public Map<TipoAlgoritmoConsenso, ConsensoResult> evaluarTodos(Hecho h, List<Hecho> universo) {
        return evaluarHecho(h, universo, EnumSet.copyOf(estrategiasPorEnum.keySet()));
    }

    /** Accesos opcionales a strategies. */
    public Optional<AlgoritmoDeConsensoStrategy> getStrategy(TipoAlgoritmoConsenso tipo) {
        return Optional.ofNullable(estrategiasPorEnum.get(tipo));
    }
    public Optional<AlgoritmoDeConsensoStrategy> getStrategy(String codigo) {
        return Optional.ofNullable(estrategiasPorCodigo.get(codigo));
    }
}