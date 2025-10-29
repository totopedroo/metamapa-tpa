package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.repositories.IHechosDinamicosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HechosService implements IHechosService {

        private final IHechosDinamicosRepository hechosRepository;
        private final ApiDinamicaMapper apiMapper;

        /** Devuelve dominio para el Adapter del agregador */
        public List<Hecho> buscar(String criterio) {
                List<Hecho> all = hechosRepository.findAll();
                if (criterio == null || criterio.isBlank()) return all;
                String c = criterio.toLowerCase();

                return all.stream().filter(h ->
                        (h.getTitulo() != null && h.getTitulo().toLowerCase().contains(c)) ||
                                (h.getDescripcion() != null && h.getDescripcion().toLowerCase().contains(c)) ||
                                (h.getCategoria() != null && h.getCategoria().toLowerCase().contains(c))
                ).toList();
        }

        /** Para el controlador propio de la fuente (DTOs internos) */
        public List<HechosOutputDto> filtrarHechos(
                String categoria,
                LocalDate fechaReporteDesde, LocalDate fechaReporteHasta,
                LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta,
                Double latitud, Double longitud) {

                List<Hecho> hechos = hechosRepository.findAll();

                return hechos.stream()
                        .filter(h -> categoria == null || h.getCategoria().equalsIgnoreCase(categoria))
                        .filter(h -> fechaReporteDesde == null || !h.getFechaCarga().isBefore(fechaReporteDesde))
                        .filter(h -> fechaReporteHasta == null || !h.getFechaCarga().isAfter(fechaReporteHasta))
                        .filter(h -> fechaAcontecimientoDesde == null || !h.getFechaAcontecimiento().isBefore(fechaAcontecimientoDesde))
                        .filter(h -> fechaAcontecimientoHasta == null || !h.getFechaAcontecimiento().isAfter(fechaAcontecimientoHasta))
                        .filter(h -> latitud == null || Objects.equals(h.getLatitud(), latitud))
                        .filter(h -> longitud == null || Objects.equals(h.getLongitud(), longitud))
                        .map(apiMapper::toOutput)                         // <- ANTES: this::convertirDto
                        .collect(Collectors.toList());                    // usa Collectors si tu JDK < 16
        }



   /*     public HechosOutputDto obtenerHecho(Long id) {
                Hecho hecho = hechosRepository.findById(id);
                return (hecho == null) ? null : apiMapper.toOutput(hecho);
        }*/
}
