package ar.edu.utn.frba.server.servicioAgregador.domain.consenso;

import ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsensoService {

    private static final Logger log = LoggerFactory.getLogger(ConsensoService.class);

    private final Map<TipoAlgoritmoConsenso, AlgoritmoDeConsensoStrategy> estrategiasPorEnum;
    private final Map<String, AlgoritmoDeConsensoStrategy> estrategiasPorCodigo;

    public ConsensoService(List<AlgoritmoDeConsensoStrategy> estrategias) {
        this.estrategiasPorEnum   = new EnumMap<>(TipoAlgoritmoConsenso.class);
        this.estrategiasPorCodigo = new HashMap<>();

        if (estrategias == null || estrategias.isEmpty()) {
            log.warn("No se recibieron strategies de consenso. ¿Falta @Component en alguna?");
        }

        for (AlgoritmoDeConsensoStrategy e : estrategias) {
            // Normalizamos el "codigo" del strategy al canonical del enum
            TipoAlgoritmoConsenso tipo = TipoAlgoritmoConsenso.fromCodigo(e.codigo());
            String canonical = tipo.codigo(); // siempre "defecto", "mayoriaSimple", etc. (camelCase)

            estrategiasPorEnum.put(tipo, e);
            estrategiasPorCodigo.put(canonical, e);
        }

        log.info("Strategies de consenso registradas: {}", estrategiasPorEnum.keySet());
    }

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

    public ConsensoResult evaluarHecho(Hecho h, List<Hecho> universo, TipoAlgoritmoConsenso algoritmo) {
        AlgoritmoDeConsensoStrategy strat = estrategiasPorEnum.get(algoritmo);
        if (strat == null) throw new IllegalArgumentException("Algoritmo no registrado: " + algoritmo);
        return strat.evaluar(h, universo);
    }

    public Map<TipoAlgoritmoConsenso, ConsensoResult> evaluarTodos(Hecho h, List<Hecho> universo) {
        return evaluarHecho(h, universo, EnumSet.copyOf(estrategiasPorEnum.keySet()));
    }

    public Optional<AlgoritmoDeConsensoStrategy> getStrategy(TipoAlgoritmoConsenso tipo) {
        return Optional.ofNullable(estrategiasPorEnum.get(tipo));
    }

    public Optional<AlgoritmoDeConsensoStrategy> getStrategy(String codigo) {
        if (codigo == null) return Optional.empty();
        // Normalizamos cualquier variante: DEFECTO / defecto / mayoria_simple / MAYORIA_SIMPLE
        try {
            TipoAlgoritmoConsenso t = TipoAlgoritmoConsenso.fromCodigo(codigo);
            return Optional.ofNullable(estrategiasPorEnum.get(t));
        } catch (IllegalArgumentException ex) {
            // fallback a búsqueda directa por clave canonical por si acaso
            String norm = codigo.trim().toLowerCase().replace("_", "");
            return Optional.ofNullable(estrategiasPorCodigo.get(norm));
        }
    }
}
