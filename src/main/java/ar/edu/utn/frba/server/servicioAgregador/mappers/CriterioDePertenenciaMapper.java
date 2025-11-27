package ar.edu.utn.frba.server.servicioAgregador.mappers;

import ar.edu.utn.frba.server.servicioAgregador.dtos.CriterioDePertenenciaDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;

import java.time.LocalDate;

public final class CriterioDePertenenciaMapper {
    private CriterioDePertenenciaMapper() {}

    public static CriterioDePertenencia toDomain(CriterioDePertenenciaDto dto) {
        if (dto == null) throw new IllegalArgumentException("CriterioDePertenenciaDto no puede ser null");

        String t = safe(dto.getTipo()).toLowerCase();
        String col = safe(dto.getColumna());

        switch (t) {
            case "texto" -> {
                String val = safe(dto.getValor());
                if (val.isEmpty()) throw new IllegalArgumentException("valor vacío para criterio 'texto'");
                return new CriterioDePertenencia(col, val);
            }
            case "fecha" -> {
                LocalDate desde = dto.getDesde();
                LocalDate hasta = dto.getHasta();
                if (desde == null || hasta == null)
                    throw new IllegalArgumentException("desde/hasta requeridos para criterio 'fecha'");
                return new CriterioDePertenencia(col, desde, hasta);
            }
            default -> throw new IllegalArgumentException("tipo inválido: " + dto.getTipo() + " (use 'texto' | 'fecha')");
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
