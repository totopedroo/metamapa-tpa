package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("hechosAgregadorService")
public class HechosService implements IHechosService {

        @Autowired
        private IHechosRepository hechosRepository;

        @Autowired
        private NormalizadorService normalizadorService;
        private static final String apiUrl = "https://api-ddsi.disilab.ar/docs/API_Desastres_Naturales.postman_collection.json";

        @Autowired
    private RestTemplate restTemplate;

        // --- uso la factory centralizada ---
        @Override
        public HechosOutputDto convertirDto(Hecho hecho) {
                return HechosOutputDto.fromModel(hecho);
        }

        // Filtra contra el repositorio (no contra una lista inexistente) y excluye eliminados
        @Override
        public List<HechosOutputDto> filtrarHechos(String categoria,
                                                   LocalDate fechaReporteDesde,
                                                   LocalDate fechaReporteHasta,
                                                   LocalDate fechaAcontecimientoDesde,
                                                   LocalDate fechaAcontecimientoHasta,
                                                   Double latitud,
                                                   Double longitud) {

                return hechosRepository.findAll().stream()
                        .filter(h -> !h.estaEliminado()) // no mostrar hechos con solicitud aceptada
                        .filter(h -> categoria == null || safeEqIgnoreCase(h.getCategoria(), categoria))
                        .filter(h -> fechaReporteDesde == null || !safeDate(h.getFechaCarga()).isBefore(fechaReporteDesde))
                        .filter(h -> fechaReporteHasta == null || !safeDate(h.getFechaCarga()).isAfter(fechaReporteHasta))
                        .filter(h -> fechaAcontecimientoDesde == null || !safeDate(h.getFechaAcontecimiento()).isBefore(fechaAcontecimientoDesde))
                        .filter(h -> fechaAcontecimientoHasta == null || !safeDate(h.getFechaAcontecimiento()).isAfter(fechaAcontecimientoHasta))
                        .filter(h -> latitud == null  || Objects.equals(h.getLatitud(),  latitud))
                        .filter(h -> longitud == null || Objects.equals(h.getLongitud(), longitud))
                        .map(HechosOutputDto::fromModel) // factory simple → cero ambigüedad
                        .collect(Collectors.toList());
        }

        @Override
        public HechosOutputDto crearHecho(HechosInputDto inputDto) {
                // Normalizar datos de entrada
                String tituloNormalizado = normalizadorService.normalizarTitulo(inputDto.getTitulo());
                String descripcionNormalizada = normalizadorService.normalizarDescripcion(inputDto.getDescripcion());
                String categoriaNormalizada = normalizadorService.normalizarCategoria(inputDto.getCategoria());
                String provinciaNormalizada = normalizadorService.normalizarProvincia(inputDto.getProvincia());
                Double latitudNormalizada = normalizadorService.normalizarLatitud(inputDto.getLatitud());
                Double longitudNormalizada = normalizadorService.normalizarLongitud(inputDto.getLongitud());

                Hecho hecho = new Hecho();
                hecho.setTitulo(tituloNormalizado);
                hecho.setDescripcion(descripcionNormalizada);
                hecho.setCategoria(categoriaNormalizada);
                hecho.setContenidoMultimedia(inputDto.getContenidoMultimedia());
                hecho.setLatitud(latitudNormalizada);
                hecho.setLongitud(longitudNormalizada);
                hecho.setFechaAcontecimiento(inputDto.getFechaAcontecimiento());
                hecho.setHoraAcontecimiento(inputDto.getHoraAcontecimiento());
                hecho.setProvincia(provinciaNormalizada);
                hecho.setFechaCarga(LocalDate.now());
                hecho.setEliminado(false);
                hecho.setConsensuado(false);

                Hecho hechoGuardado = hechosRepository.save(hecho);
                return HechosOutputDto.fromModel(hechoGuardado);
        }

        @Override
        public HechosOutputDto obtenerHecho(Long id) {
                Hecho hecho = hechosRepository.findById(id).orElse(null);
                if (hecho == null) return null;
                return HechosOutputDto.fromModel(hecho);
        }


        @Transactional
        public List<Hecho> importarDesdeApi() {
                DesastreDto[] body = restTemplate.getForObject(apiUrl, DesastreDto[].class);

                List<Hecho> aGuardar = new ArrayList<>();
                if (body == null || body.length == 0) return aGuardar;

                for (DesastreDto d : body) {
                        LocalDate fecha = d.getFechaHecho() != null ? d.getFechaHecho().toLocalDate() : null;
                        LocalTime hora  = d.getFechaHecho() != null ? d.getFechaHecho().toLocalTime() : null;

                        String titulo = trimToLen(nvl(d.getTitulo()), 50); // tu columna es Char(50)
                        String descripcion = nvl(d.getDescripcion());
                        String categoria = nvl(d.getCategoria());

                     /*   boolean existe = hechosRepository.existsByTituloAndFecha(
                                titulo, fecha, d.getLatitud(), d.getLongitud()
                        );
                        if (existe) continue;
*/
                        Hecho h = Hecho.builder()
                                .titulo(titulo)
                                .descripcion(descripcion)
                                .categoria(categoria)
                                .latitud(d.getLatitud())
                                .longitud(d.getLongitud())
                                .fechaAcontecimiento(fecha)
                                .fechaCarga(LocalDate.now())
                                .build();

                        aGuardar.add(h);
                }

                return hechosRepository.saveAll(aGuardar);
        }

        private static String nvl(String s) {
                return s == null ? "" : s;
        }

        private static String trimToLen(String s, int max) {
                if (s == null) return null;
                return s.length() <= max ? s : s.substring(0, max);
        }


        @Override
        public void setConsensuado(Hecho hecho, EstadoConsenso estado) {
                if (hecho == null) return;
                hecho.setEstadoConsenso(estado);
                hechosRepository.save(hecho);
        }

        // --- helpers ---
        private static boolean safeEqIgnoreCase(String a, String b) {
                return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
        }

        private static LocalDate safeDate(LocalDate d) {
                return d == null ? LocalDate.MIN : d;
        }
}
