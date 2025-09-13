package ar.edu.utn.frba.Server.Servicio_Agregador.Service;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.*;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.TipoFuente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.ImportadorCSV;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.ColeccionRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.IColeccionRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.CuradaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service("coleccionService")
public class ColeccionService implements IColeccionService {
    @Autowired
    @Qualifier("coleccionRepository")
    private final ColeccionRepository coleccionRepository;
    private final IHechosRepository hechosRepository;

    private final ImportadorCSV importadorCSV;


    private final ModoNavegacionStrategy irrestricta;
    private final ModoNavegacionStrategy curada;


    @Autowired
    public ColeccionService(ColeccionRepository coleccionRepository,
                            IHechosRepository hechosRepository,
                            @Qualifier("importadorColeccionesCsv") ImportadorCSV importadorCSV,
                            @Qualifier("irrestricta") ModoNavegacionStrategy irrestricta,
                            @Qualifier("curada") ModoNavegacionStrategy curada) {
        this.coleccionRepository = coleccionRepository;
        this.hechosRepository = hechosRepository;
        this.importadorCSV = importadorCSV;
        this.irrestricta = irrestricta;
        this.curada = curada;
    }


    @Autowired
    private ImportadorAPI importadorAPI;





    @Autowired
    private CuradaStrategy curadaStrategy;

    @Autowired
    private IrrestrictaStrategy irrestrictaStrategy;
    private Importador importador;



    @Override
    public List<Coleccion> findAll() {
        return coleccionRepository.findAll();
    }

    public ColeccionOutputDto coleccionOutputDto(Coleccion coleccion) {
        var dto = new ColeccionOutputDto();
        dto.setId(coleccion.getId());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());

        dto.setHechos(
                coleccion.getHechos() == null ? List.of()
                        : coleccion.getHechos().stream()
                        .map(HechosOutputDto::fromModel)
                        .collect(Collectors.toList())
        );

        // puedes usar el adaptador para no tocar más código:
        dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());

        dto.setAlgoritmoDeConsenso(
                coleccion.getAlgoritmoDeConsenso() == null ? null
                        : coleccion.getAlgoritmoDeConsenso().getClass().getSimpleName()
        );

        return dto;
    }
    @Override
    public Coleccion findByIdOrThrow(String id) {
        Coleccion c = coleccionRepository.findById(id);
        if (c == null) throw new NoSuchElementException("Colección no encontrada: " + id);
        return c;
    }
    @Override
    public ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + coleccionId);
        }

        Hecho hecho = hechosRepository.findById(hechoId);
        if (hecho == null) {
            throw new RuntimeException("Hecho no encontrado con ID: " + hechoId);
        }


        if (!coleccion.getHechos().contains(hecho)) {
            coleccion.getHechos().add(hecho);
            coleccionRepository.save(coleccion);
        }

        return ColeccionOutputDto.fromModel(coleccion);
    }


    @Override
    public List<Hecho> navegarHechos(String id, String modo) {
        Coleccion coleccion = findByIdOrThrow(id);


        String key = (modo == null) ? "irrestricta" : modo.trim().toLowerCase();
        ModoNavegacionStrategy strategy = switch (key) {
            case "curada" -> curada;
            case "irrestricta", "" -> irrestricta;
            default -> throw new IllegalArgumentException("Modo de navegación inválido: " + modo);
        };


        List<Hecho> base = (coleccion.getHechos() == null) ? List.of() : coleccion.getHechos();
        return strategy.filtrar(base);
    }

    /*public List<Hecho> obtenerHechosPorColeccion(String idColeccion) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + idColeccion);
        }
        return coleccion.getHechos().stream()
                .map(Hecho::fromModel)
                .collect(Collectors.toList());
    }*/
    public List<Hecho> obtenerHechosPorColeccion(String idColeccion) {
      Coleccion coleccion = (coleccionRepository.findById(idColeccion));

  /*      if (coleccionOpt.isEmpty()) {
            throw new NoSuchElementException("Colección no encontrada con ID: " + idColeccion);
        }
*/


        return new ArrayList<>(coleccion.getHechos());
    }


    public Coleccion setColeccionApi() {

        List<Hecho> hechosImportados;

        Fuente fuente = new Fuente(
                "https://api-ddsi.disilab.ar/public/api/desastres",
                this.importadorAPI,
                TipoFuente.PROXY
        );

        hechosImportados = this.importadorAPI.importar(fuente);

        Coleccion coleccion = new Coleccion(
                UUID.randomUUID().toString(),
                "COLECCION API",
                "Colección creada a partir de datos de API",
                new ArrayList<>()
        );

        if (hechosImportados != null) {
            for (Hecho hecho : hechosImportados) {
                if (hecho != null) {
                    coleccion.setHecho(hecho);
                    hechosRepository.save(hecho);
                }
            }
        }

        coleccionRepository.save(coleccion);
        return coleccion;
    }


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


    public void setAlgoritmoDeConsenso(String idColeccion, AlgoritmoDeConsensoStrategy algoritmo) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        coleccion.setAlgoritmoDeConsenso(algoritmo);
        coleccionRepository.save(coleccion);
    }

@Override
public Coleccion setColeccionCsv(String archivoCsvStream) {
    List<Hecho> hechosImportadosCSV;

    if (archivoCsvStream != null) {
        hechosImportadosCSV = this.importadorCSV.importar(archivoCsvStream);
    } else {
        System.err.println("Archivo CSV no encontrado en el classpath.");
        throw new RuntimeException("El archivo archivodefinitivo.csv no fue encontrado.");
    }

    Coleccion coleccionCSV = new Coleccion(
            UUID.randomUUID().toString(),
            "COLECCION CSV",
            "Colección creada a partir de datos de CSV",
            new ArrayList<>()
    );

    if (hechosImportadosCSV != null && !hechosImportadosCSV.isEmpty()) {

        for (Hecho hecho : hechosImportadosCSV) {
            if (hecho != null) {
                coleccionCSV.setHecho(hecho);
                hecho.setFuente(TipoFuente.LOCAL);
                hecho.setFechaCarga(LocalDate.now());
                hechosRepository.save(hecho);
            }
        }
    }

    coleccionRepository.save(coleccionCSV);
    return coleccionCSV;
}


    public Coleccion crearColeccionDesdeCSVHardcoded(String nombreColeccion) {
        // LEE SIEMPRE desde /archivodefinitivo.csv en resources
        List<Hecho> hechos = importadorCSV.importar("/archivodefinitivo.csv");

        // Persistir hechos en repo in-memory (asigna IDs si aplica)
        for (Hecho h : hechos) {
            if (h != null) hechosRepository.save(h);
        }

        Coleccion c = new Coleccion();
        c.setId(UUID.randomUUID().toString()); // UUID String
        c.setTitulo(nombreColeccion);
        c.setDescripcion("Colección creada desde CSV (classpath hardcoded)");
        c.setHechos(new ArrayList<>(hechos));

        coleccionRepository.save(c);
        return c;
    }
    public Hecho consensuarHecho(String coleccionId, Long hechoId) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId);
        if (coleccion == null) throw new NoSuchElementException("Colección no encontrada: " + coleccionId);

        Hecho hecho = coleccion.getHechos().stream()
                .filter(h -> Objects.equals(h.getIdHecho(), hechoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Hecho no encontrado: " + hechoId));

        hecho.setConsensuado(Optional.of(true));
        coleccionRepository.save(coleccion); // upsert (removeIf + add)
        return hecho;
    }
    public Hecho agregarFuenteAHecho(String coleccionId, Long hechoId, TipoFuente tipoFuente) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId);
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

        hecho.setTipoFuente(tipoFuente);

        // Persistir cambios de la colección (upsert)
        coleccionRepository.save(coleccion);

        // (Opcional) Si guardás Hecho también en su repo:
        if (hechosRepository != null) {
            hechosRepository.save(hecho);
        }

        return hecho;
    }
    public Hecho quitarFuenteDeHecho(String coleccionId, Long hechoId) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId);
        if (coleccion == null) {
            throw new NoSuchElementException("Colección no encontrada: " + coleccionId);
        }

        // null-safe: evita NPE si idHecho es null
        Hecho hecho = coleccion.getHechos().stream()
                .filter(h -> h != null && java.util.Objects.equals(h.getIdHecho(), hechoId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Hecho no encontrado: " + hechoId));

        // Quitar la fuente
        hecho.setTipoFuente(null);

        // Guardar colección (upsert)
        coleccionRepository.save(coleccion);

        // (Opcional) Si además guardás Hecho en su repo:
        if (hechosRepository != null) {
            hechosRepository.save(hecho);
        }

        return hecho;
    }
    public List<Hecho> filtrarHechosPorColeccion(String coleccionId, String titulo, String categoria) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId);
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