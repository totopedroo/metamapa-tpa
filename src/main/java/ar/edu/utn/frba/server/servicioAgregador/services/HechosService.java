package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
// import ar.edu.utn.frba.server.servicioAgregador.domain.DesastreDto; // ELIMINADO: No existe
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechoDTO;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("hechosAgregadorService")
@RequiredArgsConstructor
public class HechosService implements IHechosService {

  private final IHechosRepository hechosRepository;
  private final NormalizadorService normalizadorService;
  private final RestTemplate restTemplate;

  private static final String apiUrl = "https://api-ddsi.disilab.ar/docs/API_Desastres_Naturales.postman_collection.json";

  // --- MÉTODOS ORIGINALES ---

  @Override
  public HechosOutputDto convertirDto(Hecho hecho) {
    return HechosOutputDto.fromModel(hecho);
  }

  @Override
  public List<HechosOutputDto> filtrarHechos(String categoria,
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
        .filter(h -> fechaAcontecimientoDesde == null || !safeDate(h.getFechaAcontecimiento()).isBefore(fechaAcontecimientoDesde))
        .filter(h -> fechaAcontecimientoHasta == null || !safeDate(h.getFechaAcontecimiento()).isAfter(fechaAcontecimientoHasta))
        .filter(h -> latitud == null || Objects.equals(h.getLatitud(), latitud))
        .filter(h -> longitud == null || Objects.equals(h.getLongitud(), longitud))
        .map(HechosOutputDto::fromModel)
        .collect(Collectors.toList());
  }

  @Override
  public HechosOutputDto crearHecho(HechosInputDto inputDto) {
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
    // CORRECCIÓN: Como no tienes DesastreDto, dejamos esto vacío o comentado
    // para que compile. Si necesitas importar, deberás crear la clase DesastreDto.
    System.out.println("Funcionalidad de importación deshabilitada temporalmente (Falta DTO)");
    return new ArrayList<>();

        /*
        DesastreDto[] body = restTemplate.getForObject(apiUrl, DesastreDto[].class);
        List<Hecho> aGuardar = new ArrayList<>();
        if (body == null || body.length == 0) return aGuardar;

        for (DesastreDto d : body) {
            LocalDate fecha = d.getFechaHecho() != null ? d.getFechaHecho().toLocalDate() : null;

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
        */
  }

  @Override
  public void setConsensuado(Hecho hecho, EstadoConsenso estado) {
    if (hecho == null) return;
    hecho.setEstadoConsenso(estado);
    hechosRepository.save(hecho);
  }

  // --- NUEVO MÉTODO PARA LANDING PAGE ---

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

  // --- HELPERS ---

  private static String nvl(String s) {
    return s == null ? "" : s;
  }

  private static String trimToLen(String s, int max) {
    if (s == null) return null;
    return s.length() <= max ? s : s.substring(0, max);
  }

  private static boolean safeEqIgnoreCase(String a, String b) {
    return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
  }

  private static LocalDate safeDate(LocalDate d) {
    return d == null ? LocalDate.MIN : d;
  }
}