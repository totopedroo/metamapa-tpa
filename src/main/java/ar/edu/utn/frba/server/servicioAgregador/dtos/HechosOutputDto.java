package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.ConsensoResult;
import ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HechosOutputDto {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private TipoFuente fuente;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private List<Etiqueta> etiquetas;
    private List<HechoConsensoDto> consensos;   // múltiples algoritmos
    private HechoConsensoDto consensoActivo;    // opcional: el de navegación curada


    public HechosOutputDto(Long idHecho, String titulo, String descripcion, String categoria,
                           Optional<ContenidoMultimedia> contenidoMultimedia,
                           Double latitud, Double longitud, LocalDate fechaAcontecimiento,
                           LocalDate fechaCarga, List<String> etiquetas,
                           List<SolicitudEliminacion> solicitudes, Contribuyente contribuyente) {
    }

    public HechosOutputDto() {  }

    /** Básico: sólo datos del hecho (sin consensos). */
    public static HechosOutputDto fromModel(Hecho h) {
        HechosOutputDto dto = new HechosOutputDto();
        if (h == null) return dto;
        dto.setIdHecho(h.getIdHecho());
        dto.setTitulo(h.getTitulo());
        dto.setDescripcion(h.getDescripcion());
        dto.setCategoria(h.getCategoria());
        dto.setFuente(null);
        dto.setLatitud(h.getLatitud());
        dto.setLongitud(h.getLongitud());
        dto.setFechaAcontecimiento(h.getFechaAcontecimiento());
        dto.setEtiquetas(h.getEtiquetas());
        return dto;
    }

    /** Con múltiples consensos (map de algoritmo → resultado). */
    public static HechosOutputDto fromModel(Hecho h, Map<TipoAlgoritmoConsenso, ConsensoResult> resultados) {
        HechosOutputDto dto = fromModel(h);
        if (resultados != null && !resultados.isEmpty()) {
            List<HechoConsensoDto> lista = new ArrayList<>();
            for (Map.Entry<TipoAlgoritmoConsenso, ConsensoResult> e : resultados.entrySet()) {
                var alg = e.getKey();
                var res = e.getValue();
                lista.add(new HechoConsensoDto(
                        alg.name(),
                        res.getEstado(),
                        res.getSoportes(),
                        res.getTotalFuentes()
                ));
            }
            dto.setConsensos(lista);
        }
        return dto;
    }

    /** Con múltiples consensos + marca el algoritmo activo (para navegación curada). */
    public static HechosOutputDto fromModel(Hecho h,
                                            Map<TipoAlgoritmoConsenso, ConsensoResult> resultados,
                                            TipoAlgoritmoConsenso algoritmoActivo) {
        HechosOutputDto dto = fromModel(h, resultados);
        if (algoritmoActivo != null && resultados != null) {
            ConsensoResult res = resultados.get(algoritmoActivo);
            if (res != null) {
                dto.setConsensoActivo(new HechoConsensoDto(
                        algoritmoActivo.name(),
                        res.getEstado(),
                        res.getSoportes(),
                        res.getTotalFuentes()
                ));
            }
        }
        return dto;
    }
}