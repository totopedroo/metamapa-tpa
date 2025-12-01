package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Contribuyente;
import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechoDTO;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IContribuyenteRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("hechosAgregadorService")
@RequiredArgsConstructor
public class HechosService implements IHechosService {

    @Autowired
    private IHechosRepository hechosRepository;

    @Autowired
    private IContribuyenteRepository contribuyenteRepository;

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

    @Override
    public List<HechosOutputDto> filtrarHechos(
            String categoria,
            LocalDate fechaReporteDesde,
            LocalDate fechaReporteHasta,
            LocalDate fechaAcontecimientoDesde,
            LocalDate fechaAcontecimientoHasta,
            Double latitud,
            Double longitud) {

        return hechosRepository.findAll().stream()
                .filter(h -> !h.estaEliminado())
                .filter(h -> categoria == null || safeEqIgnoreCase(h.getCategoria(), categoria))
                .filter(h -> fechaReporteDesde == null || !safeDate(h.getFechaCarga()).isBefore(fechaReporteDesde))
                .filter(h -> fechaReporteHasta == null || !safeDate(h.getFechaCarga()).isAfter(fechaReporteHasta))
                .filter(h -> fechaAcontecimientoDesde == null ||
                        !safeDate(h.getFechaAcontecimiento()).isBefore(fechaAcontecimientoDesde))
                .filter(h -> fechaAcontecimientoHasta == null ||
                        !safeDate(h.getFechaAcontecimiento()).isAfter(fechaAcontecimientoHasta))
                .filter(h -> latitud == null || Objects.equals(h.getLatitud(), latitud))
                .filter(h -> longitud == null || Objects.equals(h.getLongitud(), longitud))
                .map(HechosOutputDto::fromModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Hecho> filtrarHechosPaginado(
            String categoria,
            LocalDate fechaReporteDesde,
            LocalDate fechaReporteHasta,
            LocalDate fechaAcontecimientoDesde,
            LocalDate fechaAcontecimientoHasta,
            Double latitud,
            Double longitud,
            Pageable pageable
    ) {
        return hechosRepository.filtrarHechosPaginado(
                categoria,
                fechaReporteDesde,
                fechaReporteHasta,
                fechaAcontecimientoDesde,
                fechaAcontecimientoHasta,
                latitud,
                longitud,
                pageable
        );
    }

    @Override
    public HechosOutputDto crearHecho(HechosInputDto inputDto) {

        Contribuyente c = contribuyenteRepository.findById(inputDto.getIdContribuyente())
                .orElseThrow(() -> new RuntimeException("Contribuyente no encontrado"));

        Hecho hecho = new Hecho();
        hecho.setTitulo(normalizadorService.normalizarTitulo(inputDto.getTitulo()));
        hecho.setDescripcion(normalizadorService.normalizarDescripcion(inputDto.getDescripcion()));
        hecho.setCategoria(normalizadorService.normalizarCategoria(inputDto.getCategoria()));
        hecho.setProvincia(normalizadorService.normalizarProvincia(inputDto.getProvincia()));
        hecho.setLatitud(normalizadorService.normalizarLatitud(inputDto.getLatitud()));
        hecho.setLongitud(normalizadorService.normalizarLongitud(inputDto.getLongitud()));
        hecho.setFechaAcontecimiento(inputDto.getFechaAcontecimiento());
        hecho.setHoraAcontecimiento(inputDto.getHoraAcontecimiento());
        hecho.setFechaCarga(LocalDate.now());
        hecho.setEliminado(false);
        hecho.setConsensuado(false);
        hecho.setContribuyente(c);

        Hecho guardado = hechosRepository.save(hecho);
        return HechosOutputDto.fromModel(guardado);
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

            LocalDate fecha = d.getFechaHecho() != null
                    ? d.getFechaHecho().toLocalDate()
                    : null;

            String titulo = trimToLen(nvl(d.getTitulo()), 50);
            String descripcion = nvl(d.getDescripcion());
            String categoria = nvl(d.getCategoria());

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

    @Override
    public List<HechoDTO> obtenerHechosLanding(String modo, int limit) {
    // 1. Determinar estado (0=Irrestricto, 1=Curado)
    Integer estadoDB = "curado".equalsIgnoreCase(modo) ? 1 : 0;

    // 2. Llamar al método JPQL del repositorio (asegúrate de haber actualizado IHechosRepository)
    List<Hecho> hechos = hechosRepository.findByEstadoConsensoAndEliminadoFalseOrderByFechaCargaDesc(
        estadoDB,
        PageRequest.of(0, limit)
    );

    // 3. Convertir a DTO Simple
    return hechos.stream().map(h -> HechoDTO.builder()
        .id(h.getIdHecho())
        .titulo(h.getTitulo())
        .descripcion(h.getDescripcion())
        .categoria(h.getCategoria())
        .lugar(h.getProvincia())
        .fecha(h.getFechaAcontecimiento())
        .build()
    ).collect(Collectors.toList());
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

    @Override
    public List<HechosOutputDto> listarHechosPorUsuario(Long idUsuario) {
        return hechosRepository.findByContribuyente_Id(idUsuario)
                .stream()
                .map(HechosOutputDto::fromModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean eliminarHecho(Long id) {
        Hecho hecho = hechosRepository.findById(id).orElse(null);
        if (hecho == null) return false;

        hecho.setEliminado(true);
        hechosRepository.save(hecho);
        return true;
    }

    @Override
    public Optional<Hecho> buscarPorId(Long id) {
        return hechosRepository.findById(id);
    }

    // --- helpers ---
    private static boolean safeEqIgnoreCase(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }

    private static LocalDate safeDate(LocalDate d) {
                return d == null ? LocalDate.MIN : d;
        }
}
