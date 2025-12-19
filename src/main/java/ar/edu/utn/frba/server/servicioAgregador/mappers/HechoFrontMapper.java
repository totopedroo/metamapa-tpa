package ar.edu.utn.frba.server.servicioAgregador.mappers;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechoFrontDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;

public class HechoFrontMapper {

    /**
     * Convierte desde HechosOutputDto → HechoFrontDto (para endpoints de búsqueda).
     */
    public static HechoFrontDto toDto(HechosOutputDto dto) {
        if (dto == null) return null;

        HechoFrontDto out = new HechoFrontDto();
        out.setIdHecho(dto.getIdHecho());
        out.setTitulo(dto.getTitulo());
        out.setDescripcion(dto.getDescripcion());
        out.setCategoria(dto.getCategoria());
        out.setLatitud(dto.getLatitud());
        out.setLongitud(dto.getLongitud());
        out.setFechaAcontecimiento(dto.getFechaAcontecimiento());

        // Etiquetas → lista de strings
        if (dto.getEtiquetas() != null) {
            out.setEtiquetas(
                    dto.getEtiquetas().stream()
                            .map(Etiqueta::getEtiqueta)
                            .toList()
            );
        }

        // Consensos → nombres del algoritmo
        if (dto.getConsensos() != null) {
            out.setConsensos(
                    dto.getConsensos().stream()
                            .map(c -> c.getAlgoritmo() + " (" + c.getEstado() + ")")
                            .toList()
            );
        }

        // Consensuado activo (si viene)
        out.setConsensuado(dto.getConsensoActivo() != null);

        // Fuente → lista de strings si viene
        if (dto.getFuente() != null) {
            out.setFuentes(java.util.List.of(dto.getFuente().name()));
        }
        if (dto.getContenidoMultimedia() != null) {
            // si tu HechoFrontDto tiene "contenidoMultimedia" (objeto)
            // out.setContenidoMultimedia(h.getContenidoMultimedia());

            // si tu HechoFrontDto tiene solo un campo "contenidoMultimedia" como String url:
            out.setContenidoMultimedia(dto.getContenidoMultimedia().getUrl());

        }
        return out;
    }

    /**
     * Convierte directamente desde entidad Hecho → HechoFrontDto
     * (para endpoints que trabajan con la entidad real, ej. crearHecho)
     */
    public static HechoFrontDto toDto(Hecho h) {
        if (h == null) return null;

        HechoFrontDto out = new HechoFrontDto();

        out.setIdHecho(h.getIdHecho());
        out.setTitulo(h.getTitulo());
        out.setDescripcion(h.getDescripcion());
        out.setCategoria(h.getCategoria());
        out.setFechaAcontecimiento(h.getFechaAcontecimiento());
        out.setFechaCarga(h.getFechaCarga());
        out.setHoraAcontecimiento(h.getHoraAcontecimiento());
        out.setLatitud(h.getLatitud());
        out.setLongitud(h.getLongitud());
        out.setProvincia(h.getProvincia());
        if (h.getContenidoMultimedia() != null && h.getContenidoMultimedia().getUrl() != null) {
            out.setContenidoMultimedia(h.getContenidoMultimedia().getUrl());
        } else {
            out.setContenidoMultimedia(null);
        }
        // Etiquetas
        if (h.getEtiquetas() != null) {
            out.setEtiquetas(
                    h.getEtiquetas().stream()
                            .map(Etiqueta::getEtiqueta)
                            .toList()
            );
        }

        // Contribuyente
        if (h.getContribuyente() != null) {
            out.setIdContribuyente(h.getContribuyente().getId());
        }

        // Fuentes
        if (h.getFuente() != null) {
            out.setFuentes(
                    h.getFuente().stream()
                            .map(f -> f.getTipo().name())
                            .toList()
            );
        }

        // Consenso
        out.setConsensuado(h.isConsensuado());

        return out;
    }
}