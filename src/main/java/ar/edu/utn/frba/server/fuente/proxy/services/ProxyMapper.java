package ar.edu.utn.frba.server.fuente.proxy.services;

import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Etiqueta;
import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import ar.edu.utn.frba.server.fuente.proxy.dtos.MetaMapaHechoDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ProxyMapper {

    private static final String ORIGEN_DESASTRES = "proxy-apidesastres";
    private static final String ORIGEN_METAMAPA  = "proxy-metamapa";

    // ------------------ Parsers de fecha ------------------

    private LocalDate toLocalDate(OffsetDateTime odt) {
        return (odt != null) ? odt.toLocalDate() : LocalDate.now();
    }

    private LocalDate toLocalDate(String raw) {
        if (raw == null || raw.isBlank()) return LocalDate.now();

        // intenta formatos en orden
        try { return LocalDate.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE); }
        catch (Exception ignored) {}

        try { return LocalDateTime.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate(); }
        catch (Exception ignored) {}

        try { return OffsetDateTime.parse(raw, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate(); }
        catch (Exception ignored) {}

        try { return LocalDateTime.parse(raw, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate(); }
        catch (Exception ignored) {}

        // último recurso: primeros 10 chars yyyy-MM-dd (con guardias)
        String s = raw.length() >= 10 ? raw.substring(0, 10) : raw;
        try { return LocalDate.parse(s); } catch (Exception e) { return LocalDate.now(); }
    }

    private static String safe(String s) { return (s == null) ? "" : s; }

    // ------------------ Mappers a contratos ------------------

    // API Desastres -> HechoDto
    public HechoDto toHechoDto(DesastreDto d) {
        LocalDate fecha = toLocalDate(d.getFechaHecho());

        return new HechoDto(
                d.getId(),
                safe(d.getTitulo()),
                safe(d.getDescripcion()),
                ORIGEN_DESASTRES,
                d.getCategoria(),
                d.getLatitud(),
                d.getLongitud(),
                fecha,
                List.of()
        );
    }

    // MetaMapa (otra instancia) -> HechoDto
    public HechoDto toHechoDtoMeta(MetaMapaHechoDto m) {
        LocalDate fecha = toLocalDate(m.getFechaAcontecimiento());
        List<Etiqueta> tags = m.getEtiquetas() == null ? List.of()
                : m.getEtiquetas().stream().filter(s -> s != null && !s.getEtiqueta().isBlank())
                .distinct().toList();

        return new HechoDto(
                m.getId(),
                safe(m.getTitulo()),
                safe(m.getDescripcion()),
                ORIGEN_METAMAPA,
                m.getCategoria(),
                m.getLatitud(),
                m.getLongitud(),
                fecha,
                tags
        );
    }

    // Filtro opcional usado por el adapter
    public boolean match(HechoDto h, String criterio) {
        if (criterio == null || criterio.isBlank()) return true;
        String c = criterio.toLowerCase();
        return (h.titulo() != null && h.titulo().toLowerCase().contains(c)) ||
                (h.descripcion() != null && h.descripcion().toLowerCase().contains(c));
    }
}
