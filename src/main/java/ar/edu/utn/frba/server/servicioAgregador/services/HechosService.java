package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import ar.edu.utn.frba.server.gestorUsuarios.domain.Usuario;
import ar.edu.utn.frba.server.gestorUsuarios.repository.UsuariosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.ContenidoMultimedia;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
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
    private UsuariosRepository usuarioRepository;

    @Autowired
    private NormalizadorService normalizadorService;
    private static final String apiUrl = "https://api-ddsi.disilab.ar/docs/API_Desastres_Naturales.postman_collection.json";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UsuariosRepository usuariosRepository;

    // --- uso la factory centralizada ---
    @Override
    public HechosOutputDto convertirDto(Hecho hecho) {
      return HechosOutputDto.fromModel(hecho);
    }

    @Override
    public List<HechosOutputDto> filtrarHechos(String categoria, LocalDate fechaReporteDesde, LocalDate fechaReporteHasta,
                                               LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta,
                                               Double latitud, Double longitud) {
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
    public Page<Hecho> filtrarHechosPaginado(
            String categoria,
            LocalDate fechaReporteDesde,
            LocalDate fechaReporteHasta,
            LocalDate fechaAcontecimientoDesde,
            LocalDate fechaAcontecimientoHasta,
            Double latitud,
            Double longitud,
            boolean soloConsensuados,
            Pageable pageable
    ) {
        Specification<Hecho> spec = (root, query, cb) -> cb.isFalse(root.get("eliminado"));

        if (soloConsensuados) {
            spec = spec.and((root, query, cb) -> cb.isTrue(root.get("consensuado")));
        }

        if (categoria != null && !categoria.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("categoria")), "%" + categoria.toLowerCase() + "%"));
        }

        // fechaCarga (reporte)
        if (fechaReporteDesde != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaCarga"), fechaReporteDesde));
        }
        if (fechaReporteHasta != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaCarga"), fechaReporteHasta));
        }

        // fechaAcontecimiento
        if (fechaAcontecimientoDesde != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaAcontecimiento"), fechaAcontecimientoDesde));
        }
        if (fechaAcontecimientoHasta != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaAcontecimiento"), fechaAcontecimientoHasta));
        }

        // lat/long (si tu filtro es exacto; si es “cerca de”, se hace distinto)
        if (latitud != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("latitud"), latitud));
        }
        if (longitud != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("longitud"), longitud));
        }

        return hechosRepository.findAll(spec, pageable);
    }

    @Override
    public HechosOutputDto crearHecho(HechosInputDto inputDto) {
      // 1. Normalizar datos
      String tituloNormalizado = normalizadorService.normalizarTitulo(inputDto.getTitulo());
      String descripcionNormalizada = normalizadorService.normalizarDescripcion(inputDto.getDescripcion());
      String categoriaNormalizada = normalizadorService.normalizarCategoria(inputDto.getCategoria());
      String provinciaNormalizada = normalizadorService.normalizarProvincia(inputDto.getProvincia());
      Double latitudNormalizada = normalizadorService.normalizarLatitud(inputDto.getLatitud());
      Double longitudNormalizada = normalizadorService.normalizarLongitud(inputDto.getLongitud());

      // 2. Buscar al Contribuyente (Usuario)
      Usuario contribuyente = null;
      if (inputDto.getIdContribuyente() != null) {
        contribuyente = usuariosRepository.findById(inputDto.getIdContribuyente())
            .orElseThrow(() -> new RuntimeException("Contribuyente no encontrado con ID: " + inputDto.getIdContribuyente()));
      }

      // 3. Instanciar Hecho
      Hecho hecho = new Hecho();
      hecho.setTitulo(tituloNormalizado);
      hecho.setDescripcion(descripcionNormalizada);
      hecho.setCategoria(categoriaNormalizada);
      hecho.setProvincia(provinciaNormalizada);
      hecho.setLatitud(latitudNormalizada);
      hecho.setLongitud(longitudNormalizada);

      hecho.setFechaAcontecimiento(inputDto.getFechaAcontecimiento());
      hecho.setHoraAcontecimiento(inputDto.getHoraAcontecimiento());
      hecho.setFechaCarga(LocalDate.now());

      hecho.setEliminado(false);
      hecho.setConsensuado(false);
      hecho.setEstadoConsenso(EstadoConsenso.NO_CONSENSUADO); // Valor inicial por defecto

      // 4. Asignar Contribuyente
      // (Asegúrate de que tu entidad Hecho tenga este método y acepte 'Usuario')
      if (contribuyente != null) {
        hecho.setContribuyente(contribuyente);
        // Si el setter espera un objeto 'Contribuyente' distinto de 'Usuario',
        // tendrás que adaptar esto. Asumo que Usuario es la entidad que usas.
      }

      // 5. Manejo de Multimedia (String -> Objeto)
      if (inputDto.getContenidoMultimedia() != null && !inputDto.getContenidoMultimedia().isBlank()) {
        ContenidoMultimedia cm = new ContenidoMultimedia();
        cm.setUrl(inputDto.getContenidoMultimedia());
        // Si no tienes Cascada configurada en la relación, deberías guardar 'cm' primero.
        // repositorioMultimedia.save(cm);
        hecho.setContenidoMultimedia(cm);
      }

      // 6. Guardar
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
    public List<Hecho> listarHechosPorUsuario2(Long idUsuario) {
      return hechosRepository.findByContribuyente_Id(idUsuario);
              /*
              .stream()
              .map(HechosOutputDto::fromModel)
              .collect(Collectors.toList());
               */
    }

    @Override
    @Transactional
    public boolean eliminarHecho(Long id) {
        Hecho hecho = hechosRepository.findById(id).orElse(null);
        if (hecho == null) return false;

        hechosRepository.softDelete(id);
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
