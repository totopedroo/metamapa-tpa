package ar.edu.utn.frba.server.servicioAgregador.mappers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import ar.edu.utn.frba.server.servicioAgregador.dtos.*;
import java.util.List;

public class ColeccionFrontMapper {


    /* ============================================================
       ===============   1) Desde ENTIDAD Coleccion   ==============
       ============================================================ */
    public static ColeccionFrontDto toDto(Coleccion c) {
        if (c == null) return null;

        ColeccionFrontDto out = new ColeccionFrontDto();
        out.setId(c.getId());
        out.setTitulo(c.getTitulo());
        out.setDescripcion(c.getDescripcion());

        // ============================
        //       ALGORITMO
        // ============================
        if (c.getAlgoritmoConsensoEntidad() != null) {
            out.setAlgoritmoDeConsenso(
                    c.getAlgoritmoConsensoEntidad().getNombre()  // <-- ESTE ES EL CORRECTO
            );
        } else {
            out.setAlgoritmoDeConsenso(null);
        }

        // ============================
        //          HECHOS
        // ============================
        out.setHechos(
                c.getHechos() == null ? List.of()
                        : c.getHechos().stream()
                        .map(HechoFrontMapper::toDto)
                        .toList()
        );

        // ============================
        //         CRITERIOS
        // ============================
        out.setCriterios(
                c.getCriterioDePertenencia() == null ? List.of()
                        : c.getCriterioDePertenencia().stream()
                        .map(CriterioDePertenenciaDto::fromModel)
                        .toList()
        );

        // ============================
        //           FUENTE
        // ============================
        if (c.getFuente() != null) {
            out.setFuente(c.getFuente().getTipo().name());
        } else {
            out.setFuente(null);
        }

        return out;
    }

    /* ============================================================
       =============   2) Desde ColeccionOutputDto   ===============
       ============================================================ */
    public static ColeccionFrontDto fromOutputDto(ColeccionOutputDto dto) {
        if (dto == null) return null;

        ColeccionFrontDto out = new ColeccionFrontDto();
        out.setId(dto.getId());
        out.setTitulo(dto.getTitulo());
        out.setDescripcion(dto.getDescripcion());
        out.setAlgoritmoDeConsenso(dto.getAlgoritmoDeConsenso());

        out.setHechos(
                dto.getHechos() == null ? List.of()
                        : dto.getHechos().stream()
                        .map(HechoFrontMapper::toDto)
                        .toList()
        );

        out.setCriterios(dto.getCriterios());

        out.setFuente(null);

        return out;
    }


    /* ============================================================
       =============   3) Desde ColeccionOutputBD   ===============
       ============================================================ */
    public static ColeccionFrontDto fromOutputBD(ColeccionOutputBD dto) {
        if (dto == null) return null;

        ColeccionFrontDto out = new ColeccionFrontDto();
        out.setId(dto.getId());
        out.setTitulo(dto.getTitulo());
        out.setDescripcion(dto.getDescripcion());

        out.setHechos(List.of());
        out.setCriterios(List.of());
        out.setAlgoritmoDeConsenso(null);
        out.setFuente(null);

        return out;
    }


    /* ============================================================
       ========== ALIAS PARA PODER USAR ::toDto EN STREAMS ========
       ============================================================ */

    public static ColeccionFrontDto toDto(ColeccionOutputDto dto) {
        return fromOutputDto(dto);
    }

    public static ColeccionFrontDto toDto(ColeccionOutputBD dto) {
        return fromOutputBD(dto);
    }
}