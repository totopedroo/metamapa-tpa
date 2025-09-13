package ar.edu.utn.frba.Server.ServicioEstadisticas;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.IColeccionRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.ISolicitudRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.IColeccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EstadisticasService {

    private final IColeccionRepository coleccionRepository;
    private final IHechosRepository hechosRepository;
    private final IColeccionService coleccionService;

    @Autowired(required = false)
    private ISolicitudRepository solicitudesRepository; // opcional

    @Autowired
    public EstadisticasService(IColeccionRepository coleccionRepository,
                               IHechosRepository hechosRepository, IColeccionService coleccionService) {
        this.coleccionRepository = coleccionRepository;
        this.hechosRepository = hechosRepository;
        this.coleccionService = coleccionService;
    }

    public record TopKV(String clave, long valor) {}

    public TopKV provinciaTop(String coleccionId) {
        List<Hecho> hechos = coleccionService.obtenerHechosPorColeccion(coleccionId);
        Map<String, Long> porProvincia = hechos.stream()
                .collect(Collectors.groupingBy(this::provinciaDe, Collectors.counting()));
        return toTop(porProvincia);
    }

    public TopKV categoriaTop(String coleccionId) {
        List<Hecho> hechos = coleccionService.obtenerHechosPorColeccion(coleccionId);
        Map<String, Long> porCategoria = hechos.stream()
                .collect(Collectors.groupingBy(this::categoriaDe, Collectors.counting()));
        return toTop(porCategoria);
    }

    public TopKV provinciaTopPorCategoria(String coleccionId, String categoria) {
        String cat = categoria == null ? "" : categoria.trim().toLowerCase();
        List<Hecho> filtrados = coleccionService.obtenerHechosPorColeccion(coleccionId).stream()
                .filter(h -> categoriaDe(h).toLowerCase().equals(cat))
                .collect(Collectors.toList());
        Map<String, Long> porProvincia = filtrados.stream()
                .collect(Collectors.groupingBy(this::provinciaDe, Collectors.counting()));
        return toTop(porProvincia);
    }

    public TopKV horaTopPorCategoria(String coleccionId, String categoria) {
        String cat = categoria == null ? "" : categoria.trim().toLowerCase();
        Map<Integer, Long> porHora = coleccionService.obtenerHechosPorColeccion(coleccionId).stream()
                .filter(h -> categoriaDe(h).toLowerCase().equals(cat))
                .collect(Collectors.groupingBy(this::horaDe, Collectors.counting()));

        if (porHora.isEmpty()) return new TopKV("(sin datos)", 0);

        Map.Entry<Integer, Long> max = porHora.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();
        return new TopKV(String.valueOf(max.getKey()), max.getValue());
    }

    public long solicitudesSpam() {
        if (solicitudesRepository == null) return 0L;
        try {
            try {
                Method m = solicitudesRepository.getClass().getMethod("countByEsSpamTrue");
                Object r = m.invoke(solicitudesRepository);
                if (r instanceof Number n) return n.longValue();
            } catch (NoSuchMethodException ignore) {

            }

            Method m2 = solicitudesRepository.getClass().getMethod("findAll");

            List<Object> todas = (List<Object>) m2.invoke(solicitudesRepository);
            long cnt = 0;
            for (Object s : todas) {
                try {
                    Method getSpam = s.getClass().getMethod("getEsSpam");
                    Object v = getSpam.invoke(s);
                    if (v instanceof Boolean b && Boolean.TRUE.equals(b)) cnt++;
                } catch (Exception ignored) {}
            }
            return cnt;
        } catch (Exception e) {
            return 0L;
        }
    }



    private TopKV toTop(Map<String, Long> mapa) {
        if (mapa == null || mapa.isEmpty()) return new TopKV("(sin datos)", 0);
        Map.Entry<String, Long> max = mapa.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();
        return new TopKV(max.getKey(), max.getValue());
    }

    private String provinciaDe(Hecho h) {

        try {
            Method m = h.getClass().getMethod("getProvincia");
            Object v = m.invoke(h);
            if (v != null) return String.valueOf(v);
        } catch (Exception ignored) {}

        try {
            Field f = h.getClass().getDeclaredField("provincia");
            f.setAccessible(true);
            Object v = f.get(h);
            if (v != null) return String.valueOf(v);
        } catch (Exception ignored) {}
        return "DESCONOCIDA";
    }

    private String categoriaDe(Hecho h) {

        try {
            Method m = h.getClass().getMethod("getCategoria");
            Object v = m.invoke(h);
            if (v != null) return String.valueOf(v);
        } catch (Exception ignored) {}

        try {
            Field f = h.getClass().getDeclaredField("categoria");
            f.setAccessible(true);
            Object v = f.get(h);
            if (v != null) return String.valueOf(v);
        } catch (Exception ignored) {}
        return "(sin categoria)";
    }

    private int horaDe(Hecho h) {

        try {
            Method m = h.getClass().getMethod("getFechaHora");
            Object v = m.invoke(h);
            if (v instanceof LocalDateTime ldt) return ldt.getHour();
        } catch (Exception ignored) {}

        try {
            Method m = h.getClass().getMethod("getHora");
            Object v = m.invoke(h);
            if (v instanceof Number n) return clampHour(n.intValue());
            if (v instanceof String s) return parseHora(s);
        } catch (Exception ignored) {}

        return 0;
    }

    private int parseHora(String s) {
        if (s == null) return 0;
        String t = s.trim();
        try {
            if (t.contains(":")) { // "14:30"
                return clampHour(Integer.parseInt(t.split(":")[0]));
            }
            return clampHour(Integer.parseInt(t));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int clampHour(int h) {
        return Math.max(0, Math.min(23, h));
    }
}
