package ar.edu.utn.frba.server.servicioEstadisticas.services;

import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.ISolicitudRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IColeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstadisticasService {

    @Autowired
    private IHechosRepository hechoRepository;

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Autowired
    private IColeccionRepository coleccionRepository;

    /**
     * Obtiene la provincia con mayor cantidad de hechos reportados
     */
    public Map<String, Object> obtenerProvinciaConMasHechos() {
        List<Object[]> resultados = hechoRepository.contarHechosPorProvincia();

        if (resultados.isEmpty()) {
            return Map.of("provincia", "Sin datos", "cantidad", 0);
        }

        Object[] primerResultado = resultados.get(0);
        String provincia = (String) primerResultado[0];
        Long cantidad = (Long) primerResultado[1];

        return Map.of("provincia", provincia, "cantidad", cantidad);
    }

    /**
     * Obtiene la categoría con mayor cantidad de hechos reportados
     */
    public Map<String, Object> obtenerCategoriaConMasHechos() {
        List<Object[]> resultados = hechoRepository.contarHechosPorCategoria();

        if (resultados.isEmpty()) {
            return Map.of("categoria", "Sin datos", "cantidad", 0);
        }

        Object[] primerResultado = resultados.get(0);
        String categoria = (String) primerResultado[0];
        Long cantidad = (Long) primerResultado[1];

        return Map.of("categoria", categoria, "cantidad", cantidad);
    }

    /**
     * Obtiene la provincia con mayor cantidad de hechos de una categoría específica
     */
    public Map<String, Object> obtenerProvinciaConMasHechosDeCategoria(String categoria) {
        List<Object[]> resultados = hechoRepository.contarHechosPorProvinciaYCategoria(categoria);

        if (resultados.isEmpty()) {
            return Map.of("provincia", "Sin datos", "categoria", categoria, "cantidad", 0);
        }

        Object[] primerResultado = resultados.get(0);
        String provincia = (String) primerResultado[0];
        Long cantidad = (Long) primerResultado[1];

        return Map.of("provincia", provincia, "categoria", categoria, "cantidad", cantidad);
    }

    /**
     * Obtiene la hora del día con mayor cantidad de hechos de una categoría
     * específica
     */
    public Map<String, Object> obtenerHoraConMasHechosDeCategoria(String categoria) {
        List<Object[]> resultados = hechoRepository.contarHechosPorHoraYCategoria(categoria);

        if (resultados.isEmpty()) {
            return Map.of("hora", "Sin datos", "categoria", categoria, "cantidad", 0);
        }

        Object[] primerResultado = resultados.get(0);
        String hora = primerResultado[0].toString();
        Long cantidad = (Long) primerResultado[1];

        return Map.of("hora", hora, "categoria", categoria, "cantidad", cantidad);
    }

    /**
     * Cuenta cuántas solicitudes de eliminación son spam
     */
    public Map<String, Object> contarSolicitudesSpam() {
        Long cantidadSpam = solicitudRepository.contarSolicitudesSpam();
        return Map.of("solicitudesSpam", cantidadSpam);
    }

    /**
     * Obtiene estadísticas generales del sistema
     */
    public Map<String, Object> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Estadísticas de hechos
        estadisticas.put("provinciaConMasHechos", obtenerProvinciaConMasHechos());
        estadisticas.put("categoriaConMasHechos", obtenerCategoriaConMasHechos());

        // Estadísticas de solicitudes
        estadisticas.put("solicitudesSpam", contarSolicitudesSpam());

        // Estadísticas de colecciones
        List<Object[]> coleccionesConHechos = coleccionRepository.contarHechosPorColeccion();
        estadisticas.put("coleccionesConHechos", coleccionesConHechos);

        return estadisticas;
    }

    /**
     * Obtiene estadísticas por colección específica
     */
    public Map<String, Object> obtenerEstadisticasPorColeccion(Long coleccionId) {
        Map<String, Object> estadisticas = new HashMap<>();

        // Hechos por provincia en la colección
        List<Object[]> hechosPorProvincia = hechoRepository.contarHechosPorProvincia();
        estadisticas.put("hechosPorProvincia", hechosPorProvincia);

        // Hechos por categoría en la colección
        List<Object[]> hechosPorCategoria = hechoRepository.contarHechosPorCategoria();
        estadisticas.put("hechosPorCategoria", hechosPorCategoria);

        return estadisticas;
    }
}