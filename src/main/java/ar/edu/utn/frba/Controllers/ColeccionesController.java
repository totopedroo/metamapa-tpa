package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Service.ServicioColecciones;
import ar.edu.utn.frba.Dtos.ColeccionDto;
import ar.edu.utn.frba.domain.Coleccion;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/colecciones")
@CrossOrigin("http://localhost:8080")
public class ColeccionesController {

    private final ServicioColecciones servicioColecciones;

    public ColeccionesController(ServicioColecciones servicioColecciones) {
        this.servicioColecciones = servicioColecciones;
    }

    @GetMapping
    public List<ColeccionDto> obtenerColecciones() {
        return servicioColecciones.getColecciones().stream()
                .map(ColeccionDto::new)
                .toList();
    }

    @GetMapping("/{id}/hechos")
    public List<HechosOutputDto> obtenerHechosDeColeccionConFiltros(
            @PathVariable long id,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false, name = "fecha_reporte_desde") LocalDate fechaReporteDesde,
            @RequestParam(required = false, name = "fecha_reporte_hasta") LocalDate fechaReporteHasta,
            @RequestParam(required = false, name = "fecha_acontecimiento_desde") LocalDate fechaAcontecimientoDesde,
            @RequestParam(required = false, name = "fecha_acontecimiento_hasta") LocalDate fechaAcontecimientoHasta,
            @RequestParam(required = false) String ubicacion
    ) {
        Coleccion coleccion = servicioColecciones.getColecciones().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        return coleccion.getHechosVisibles().stream()
                .filter(h -> categoria == null || h.getCategoria().equalsIgnoreCase(categoria))
                .filter(h -> fechaReporteDesde == null || !h.getFechaCarga().isBefore(fechaReporteDesde))
                .filter(h -> fechaReporteHasta == null || !h.getFechaCarga().isAfter(fechaReporteHasta))
                .filter(h -> fechaAcontecimientoDesde == null || !h.getFechaAcontecimiento().isBefore(fechaAcontecimientoDesde))
                .filter(h -> fechaAcontecimientoHasta == null || !h.getFechaAcontecimiento().isAfter(fechaAcontecimientoHasta))
                .filter(h -> ubicacion == null || coincideUbicacion(h, ubicacion)) // metodo opcional (?)
                .map(this::mapHechoToDto)
                .toList();
    }

    private HechosOutputDto mapHechoToDto(Hecho hecho) {
        HechosOutputDto dto = new HechosOutputDto();
        dto.setIdHecho(hecho.getIdHecho());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setContenidoMultimedia(hecho.getContenidoMultimedia());
        dto.setLatitud(hecho.getLatitud());
        dto.setLongitud(hecho.getLongitud());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        dto.setFechaCarga(hecho.getFechaCarga());
        dto.setEtiquetas(hecho.getEtiquetas());
        dto.setSolicitudes(hecho.getSolicitudes());
        return dto;
    }

    private boolean coincideUbicacion(Hecho hecho, String ubicacion) {
        return String.valueOf(hecho.getLatitud()).contains(ubicacion) ||
                String.valueOf(hecho.getLongitud()).contains(ubicacion);
    }

}