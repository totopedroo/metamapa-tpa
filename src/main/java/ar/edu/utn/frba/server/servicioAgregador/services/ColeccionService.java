package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.fuentes.FuentePort;
import ar.edu.utn.frba.server.fuente.estatica.domain.ImportadorCSV;
import ar.edu.utn.frba.server.fuente.estatica.services.EstaticaMapper;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
//import ar.edu.utn.frba.server.fuente.estatica.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.*;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IAdministradorRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IColeccionRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.ICriterioRepository;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.ConsensoService;
import ar.edu.utn.frba.server.contratos.enums.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.navegacion.ModoNavegacionStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static ar.edu.utn.frba.server.contratos.enums.TipoFuente.ESTATICA;

@Service("coleccionService")
public class ColeccionService implements IColeccionService {
    private final IColeccionRepository coleccionRepository;
    private final IHechosRepository hechosRepository;
    private final ImportadorAPI importadorAPI;
    private final IAdministradorRepository adminRepo;
    private final ICriterioRepository coleccionCriterioRepository;
    @Qualifier("Importadorcsvagregador")
    private ImportadorCSV importadorCSV;
    private final ModoNavegacionStrategy irrestricta; // Estrategia de navegación (inyectadas por nombre de bean)
    private final ModoNavegacionStrategy curada; // Estrategia de navegación (inyectadas por nombre de bean)
    private final ConsensoService consensoService;
    private final ConsultarHechosHandler consultarHechosHandler;
    private final AgregadorMapper agregadorMapper;
    private static final String ORIGEN_METAMAPA = "proxy-metamapa";


    @Value("${consenso.algoritmo-activo:mayoriaSimple}")
    private String algoritmoActivoCodigo; // "mayoriaSimple" | "multiplesMenciones" | "absoluta" | "defecto"

    @Autowired
    public ColeccionService(IColeccionRepository coleccionRepository,
                            IHechosRepository hechosRepository, ImportadorAPI importadorAPI, IAdministradorRepository adminRepo, ICriterioRepository coleccionCriterioRepository,
                            ConsensoService consensoService,
                            @Qualifier("irrestricta") ModoNavegacionStrategy irrestricta,
                            @Qualifier("curada") ModoNavegacionStrategy curada,
                            ConsultarHechosHandler consultarHechosHandler,
                            AgregadorMapper agregadorMapper) {
        this.coleccionRepository = coleccionRepository;
        this.hechosRepository = hechosRepository;
        this.importadorAPI = importadorAPI;
        this.adminRepo = adminRepo;
        this.coleccionCriterioRepository = coleccionCriterioRepository;
        this.consensoService = consensoService;
        this.irrestricta = irrestricta;
        this.curada = curada;
        this.consultarHechosHandler = consultarHechosHandler;
        this.agregadorMapper = agregadorMapper;
    }



    @Transactional
    public ColeccionOutputBD crear(ColeccionInputBD in) {
        // --- VALIDACIÓN DE ADMINISTRADORID (Ya robusta) ---
        if (in.getAdministradorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'administradorId' es obligatorio");
        }

        Administrador admin = adminRepo.findById(in.getAdministradorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "administradorId inexistente"));

        Coleccion c = new Coleccion();

        // --- VALIDACIÓN DE TITULO (Ya robusta) ---
        if (in.getTitulo() == null || in.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo 'titulo' es obligatorio");
        }
        c.setTitulo(in.getTitulo().trim());

        c.setDescripcion(in.getDescripcion() == null ? "" : in.getDescripcion().trim());
        c.setAdministrador(admin);

        // --- CAMBIO CLAVE 1: Usar findById para validar existencia de Hechos inmediatamente ---
        if (in.getHechosIds() != null && !in.getHechosIds().isEmpty()) {
            List<Hecho> hechos = in.getHechosIds().stream()
                    .map(id -> hechosRepository.findById(id).orElseThrow(
                            () -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "hechosIds contiene un ID (" + id + ") inexistente"
                            )
                    ))
                    .toList();
            c.getHechos().addAll(hechos);
        }

        // --- CAMBIO CLAVE 2: Usar findById para validar existencia de Criterios inmediatamente ---
        if (in.getCriteriosIds() != null && !in.getCriteriosIds().isEmpty()) {
            List<CriterioDePertenencia> criterios = in.getCriteriosIds().stream()
                    .map(id -> coleccionCriterioRepository.findById(id).orElseThrow(
                            () -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST,
                                    "criteriosIds contiene un ID (" + id + ") inexistente"
                            )
                    ))
                    .toList();
            c.getCriterioDePertenencia().addAll(criterios);
        }

        Coleccion saved = coleccionRepository.save(c);

        // --- CAMBIO CLAVE 3 (Defensivo): Evitar NPE en la salida por el administradorId ---
        Long adminId = Long.valueOf(saved.getAdministrador() != null ? saved.getAdministrador().getId() : null);

        return new ColeccionOutputBD(
                saved.getId(),
                saved.getTitulo(),
                saved.getDescripcion(),
                adminId,
                saved.getHechos().stream().map(Hecho::getIdHecho).toList(),
                saved.getCriterioDePertenencia().stream().map(CriterioDePertenencia::getId_criterio).toList()
        );
    }

    @Transactional
    public ColeccionOutputBD listar(Long id) {
        Coleccion c = coleccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada"));

        return new ColeccionOutputBD(
                c.getId(),
                c.getTitulo(),
                c.getDescripcion(),
                null,
                //c.getAdministrador() != null ? c.getAdministrador().getId() : null,
                c.getHechos().stream().map(Hecho::getIdHecho).toList(),
                c.getCriterioDePertenencia().stream().map(CriterioDePertenencia::getId_criterio).toList()
        );
    }



    @Override
    public List<Coleccion> findAll() {
        return coleccionRepository.findAll();
    }

    @Override
    public ColeccionOutputDto coleccionOutputDto(Coleccion coleccion) {
        var dto = new ColeccionOutputDto();
        dto.setId(coleccion.getId());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());

        var universo = (coleccion.getHechos() == null) ? List.<Hecho>of() : coleccion.getHechos();
        var algActivo = TipoAlgoritmoConsenso.fromCodigo(algoritmoActivoCodigo);

        // Mismos algoritmos que arriba
        var algoritmos = java.util.EnumSet.of(
                TipoAlgoritmoConsenso.MAYORIA_SIMPLE,
                TipoAlgoritmoConsenso.MULTIPLES_MENCIONES,
                TipoAlgoritmoConsenso.ABSOLUTA
        );

        dto.setHechos(
                universo.stream()
                        .map(h -> {
                            var res = consensoService.evaluarHecho(h, universo, algoritmos);
                            return HechosOutputDto.fromModel(h, res, algActivo); // ← sobrecarga multi
                        })
                        .collect(java.util.stream.Collectors.toList())
        );

        dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
        dto.setAlgoritmoDeConsenso(
                coleccion.getAlgoritmoDeConsenso() == null ? null
                        : coleccion.getAlgoritmoDeConsenso().getClass().getSimpleName()
        );
        return dto;
    }

    @Override
    public Coleccion findByIdOrThrow(Long id) {
        Coleccion c = coleccionRepository.findById(id).orElse(null);
        if (c == null) throw new NoSuchElementException("Colección no encontrada: " + id);
        return c;
    }

    @Override
    public ColeccionOutputDto agregarHechoAColeccion(Long coleccionId, Long hechoId) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElse(null);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + coleccionId);
        }

        Hecho hecho = hechosRepository.findById(hechoId).orElse(null);
        if (hecho == null) {
            throw new RuntimeException("Hecho no encontrado con ID: " + hechoId);
        }


        if (!coleccion.getHechos().contains(hecho)) {
            coleccion.getHechos().add(hecho);
            coleccionRepository.save(coleccion);
        }

        return coleccionOutputDto(coleccion);
    }

    // helper
    private static String normModo(String s) {
        if (s == null) return "irrestricta";
        return s.trim().toLowerCase().replace("_", "");
    }

    // --- Navegación de hechos mediante estrategia seleccionada por String ---
    @Override
    public List<Hecho> navegarHechos(Long id, String modo) {
        Coleccion coleccion = findByIdOrThrow(id);

        String key = normModo(modo);  // <--- normalizamos

        // Acepta "curada", "CURADA", "cu_rada", "c", etc.
        ModoNavegacionStrategy strategy = switch (key) {
            case "curada", "c" -> curada;
            case "irrestricta", "i", "" -> irrestricta;
            default -> throw new IllegalArgumentException("Modo de navegación inválido: " + modo);
        };

        List<Hecho> base = (coleccion.getHechos() == null) ? List.of() : coleccion.getHechos();
        return strategy.filtrar(base);
    }

    @Override
    public List<Hecho> obtenerHechosPorColeccion(Long idColeccion) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion).orElse(null);
        if (coleccion == null) throw new NoSuchElementException("Colección no encontrada: " + idColeccion);

        // base persistida
        List<Hecho> base = new ArrayList<>(coleccion.getHechos() == null ? List.of() : coleccion.getHechos());

        // traer MetaMapa en tiempo real
        var metaDtos = consultarHechosHandler.consultar(null).stream()
                .filter(d -> ORIGEN_METAMAPA.equalsIgnoreCase(d.fuente()))
                .map(agregadorMapper::toDomain)
                // respetar criterios de pertenencia de la colección
                .filter(h -> coleccion.getCriterioDePertenencia().stream().allMatch(c -> c.cumple(h)))
                .toList();

        // merge sin duplicar por id
        var ids = base.stream().map(Hecho::getIdHecho).collect(java.util.stream.Collectors.toSet());
        for (var h : metaDtos) if (ids.add(h.getIdHecho())) base.add(h);

        return base;
    }

    @Override
    public Coleccion crearColeccionDesdeFuentes(String titulo, String criterio) {
        Coleccion coleccion = new Coleccion(
                (titulo == null || titulo.isBlank()) ? "Colección desde Fuentes" : titulo,
                "Colección creada", // la ajustamos al final según orígenes detectados
                new ArrayList<>()
        );

        // iremos acumulando qué fuentes participaron
        EnumSet<TipoFuente> fuentesUsadas = EnumSet.noneOf(TipoFuente.class);

        List<Hecho> nuevosHechos;
        Fuente estatica = new Fuente(TipoFuente.ESTATICA);
        Fuente proxy = new Fuente(TipoFuente.PROXY);
        // ----- RUTA CSV (estática)
        if (criterio != null && (criterio.endsWith(".csv") || criterio.startsWith("classpath:"))) {


            var importador = new ImportadorCSV();
            var path = criterio.startsWith("classpath:") ? criterio.substring("classpath:".length()) : criterio;
            var hechosCsv = importador.importar(path);



            var estMapper = new EstaticaMapper();
            nuevosHechos = hechosCsv.stream()
                    .map(estMapper::toHechoDto)
                    .map(agregadorMapper::toDomain)       // al dominio del agregador
                    .peek(h -> {
                        if (h.getFuente() == null) h.setFuente(estatica);
                    })
                    .toList();

            fuentesUsadas.add(ESTATICA /* o ESTATICA */);

        } else {
            // ----- CUALQUIER FUENTE CONFIGURADA (proxy/dinámica…)
            var externos = consultarHechosHandler.consultar(criterio); // típicamente DTOs de contrato
            nuevosHechos = externos.stream()
                    .map(agregadorMapper::toDomain) // -> dominio agregador
                    .peek(h -> {
                        // si tu mapper ya setea tipoFuente, esto no hace falta;
                        // por las dudas: inferir desde algún campo (ej: fuente)
                        if (h.getFuente() == null && h.getContribuyente() == null) {
                            // si tenés algun indicio, ajustalo; si no, dejalo como PROXY por defecto
                            h.setFuente(proxy);
                        }
                    })
                    .toList();

            // Marcar conjunto de fuentes usadas según lo que traiga cada hecho
            for (var h : nuevosHechos) {
             //   if (h.getFuente() != null) fuentesUsadas.add(h.getFuente());
            }
            if (fuentesUsadas.isEmpty()) {

                fuentesUsadas.add(TipoFuente.PROXY);
            }
        }

        // Persistir y asociar
        for (var h : nuevosHechos) {
            hechosRepository.save(h);
            coleccion.setHecho(h);
        }
        coleccionRepository.save(coleccion);

        // ----- Descripción linda según orígenes
        coleccion.setDescripcion(descripcionPorFuentes(fuentesUsadas));
        coleccionRepository.save(coleccion);

        return coleccion;
    }

    @Transactional
    public Coleccion createColeccionDesdeApi(String tituloColeccion) {
        try {
            // 1) Importar hechos como entidades de servicioAgregador
            List<Hecho> hechos = importadorAPI.importarDesdeApi();

            // 2) Persistir los hechos primero para que tengan IDs de 'hecho'
            hechosRepository.saveAll(hechos);

            // 3) Crear colección y asociar
            Coleccion col = new Coleccion();
            col.setTitulo(tituloColeccion);
            col.setDescripcion("Colección creada desde API");
            col.setHechos(new ArrayList<>(hechos)); // inicializar por las dudas
            col.setAdministrador(null);
            return coleccionRepository.save(col);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Fallo creando la colección desde la API: " + e.getMessage(), e
            );
        }
    }



    /** AUX: Construye una descripción consistente según las fuentes detectadas. */
    private String descripcionPorFuentes(java.util.Set<TipoFuente> usadas) {
        // orden fijo: proxy / estática / dinámica
        java.util.List<String> partes = new java.util.ArrayList<>();

        if (usadas.contains(TipoFuente.PROXY))    partes.add("proxy");
        if (usadas.contains(ESTATICA))  partes.add("estática");
        if (usadas.contains(TipoFuente.DINAMICA)) partes.add("dinámica");

        if (partes.isEmpty()) return "Colección creada a partir de fuentes";

        if (partes.size() == 1) {
            return "Colección creada a partir de fuente " + partes.get(0);
        }
        return "Colección creada a partir de fuentes: " + String.join("/", partes);
    }

    @Override
    public void actualizarHechos(List<Hecho> nuevosHechos) {
        for (Coleccion coleccion : coleccionRepository.findAll()) {
            for (Hecho hecho : nuevosHechos) {
                if (hecho.estaEliminado())
                    continue;

                boolean cumple = coleccion.getCriterioDePertenencia()
                        .stream()
                        .allMatch(c -> c.cumple(hecho));

                if (cumple) {
                    coleccion.setHecho(hecho);
                }
            }
            coleccionRepository.save(coleccion);
        }
    }


    @Override
    public void setAlgoritmoDeConsenso(Long idColeccion, AlgoritmoDeConsensoStrategy algoritmo) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion).orElse(null);
        coleccion.setAlgoritmoDeConsenso(algoritmo);
        coleccionRepository.save(coleccion);
    }

    @Override
    public HechosOutputDto consensuarHecho(Long coleccionId, Long hechoId) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElse(null);
        if (coleccion == null) throw new NoSuchElementException("Colección no encontrada: " + coleccionId);

        var universo = (coleccion.getHechos() == null) ? List.<Hecho>of() : coleccion.getHechos();

        Hecho hecho = universo.stream()
                .filter(h -> Objects.equals(h.getIdHecho(), hechoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Hecho no encontrado: " + hechoId));

        // algoritmos que querés calcular
        EnumSet<TipoAlgoritmoConsenso> algoritmos = EnumSet.of(
                TipoAlgoritmoConsenso.MAYORIA_SIMPLE,
                TipoAlgoritmoConsenso.MULTIPLES_MENCIONES,
                TipoAlgoritmoConsenso.ABSOLUTA,
                TipoAlgoritmoConsenso.DEFECTO
        );

        // Ejecuta todos y te devuelve un Map<Algoritmo, ConsensoResult>
        var resultados = consensoService.evaluarHecho(hecho, universo, algoritmos);

        // Algoritmo “activo” para navegación curada (desde config o la colección)
        TipoAlgoritmoConsenso algActivo = TipoAlgoritmoConsenso.fromCodigo(algoritmoActivoCodigo);

        // “sobrecarga con múltiples algoritmos”
        return HechosOutputDto.fromModel(hecho, resultados, algActivo);
    }

    @Override
    public Hecho agregarFuenteAHecho(Long coleccionId, Long hechoId, Fuente tipoFuente) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElse(null);
        if (coleccion == null) {
            throw new NoSuchElementException("Colección no encontrada: " + coleccionId);
        }
        if (tipoFuente == null) {
            throw new IllegalArgumentException("tipoFuente es obligatorio.");
        }

        Hecho hecho = coleccion.getHechos().stream()
                .filter(h -> h != null && Objects.equals(h.getIdHecho(), hechoId)) // null-safe
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Hecho no encontrado: " + hechoId));

        hecho.setFuente(tipoFuente);

        // Persistir cambios de la colección (upsert)
        coleccionRepository.save(coleccion);

        // (Opcional) Si guardás Hecho también en su repo:
        if (hechosRepository != null) {
            hechosRepository.save(hecho);
        }

        return hecho;
    }

    @Override
    public Hecho quitarFuenteDeHecho(Long coleccionId, Long hechoId) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElse(null);
        if (coleccion == null) {
            throw new NoSuchElementException("Colección no encontrada: " + coleccionId);
        }

        // null-safe: evita NPE si idHecho es null
        Hecho hecho = coleccion.getHechos().stream()
                .filter(h -> h != null && java.util.Objects.equals(h.getIdHecho(), hechoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Hecho no encontrado: " + hechoId));

        // Quitar la fuente
        hecho.setFuente(null);

        // Guardar colección (upsert)
        coleccionRepository.save(coleccion);

        // (Opcional) Si además guardás Hecho en su repo:
        if (hechosRepository != null) {
            hechosRepository.save(hecho);
        }

        return hecho;
    }

    @Override
    public List<Hecho> filtrarHechosPorColeccion(Long coleccionId, String titulo, String categoria) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElse(null);
        if (coleccion == null) {
            throw new NoSuchElementException("Colección no encontrada: " + coleccionId);
        }

        String tituloNorm = titulo == null ? null : titulo.trim().toLowerCase();
        String catNorm    = categoria == null ? null : categoria.trim().toLowerCase();

        return coleccion.getHechos().stream()
                .filter(Objects::nonNull)
                .filter(h -> {
                    if (tituloNorm == null || tituloNorm.isEmpty()) return true;
                    String ht = h.getTitulo() == null ? "" : h.getTitulo().toLowerCase();
                    return ht.contains(tituloNorm);
                })
                .filter(h -> {
                    if (catNorm == null || catNorm.isEmpty()) return true;
                    String hc = h.getCategoria() == null ? "" : h.getCategoria().toLowerCase();
                    return hc.equals(catNorm); // si querés “contiene”, cambiá por contains
                })
                .toList();
    }
}