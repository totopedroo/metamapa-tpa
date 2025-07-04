package ar.edu.utn.frba.Servicio_Agregador.Service;

import ar.edu.utn.frba.Servicio_Agregador.Domain.*;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.ImportadorCSV;
import ar.edu.utn.frba.Servicio_Agregador.Repository.HechosRepository;
import ar.edu.utn.frba.Servicio_Agregador.Repository.IColeccionRepository;
import ar.edu.utn.frba.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.CuradaStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("coleccionService")
public class ColeccionService implements IColeccionService {

    @Autowired
    @Qualifier("coleccionRepository")
    private IColeccionRepository coleccionRepository;

    @Autowired
    private ImportadorAPI importadorAPI;

    @Autowired
    private ImportadorCSV importadorCSV;

    @Autowired
    @Qualifier("hechosAgregadorRepository")
    private IHechosRepository hechosRepository;

    @Autowired
    private CuradaStrategy curadaStrategy;

    @Autowired
    private IrrestrictaStrategy irrestrictaStrategy;
    private Importador importador;



    @Override
    public List<ColeccionOutputDto> buscarTodos() {
        List<Coleccion> colecciones = coleccionRepository.findAll();
        return colecciones.stream().map(this::coleccionOutputDto).toList();
    }

    public ColeccionOutputDto coleccionOutputDto(Coleccion coleccion) {
        var dto = new ColeccionOutputDto();
        dto.setId(coleccion.getId());
        dto.setHechos(coleccion.getHechos());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
        dto.setAlgoritmoDeConsenso(coleccion.getAlgoritmoDeConsenso());
        return dto;
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

        Fuente fuente = new Fuente("https://api-ddsi.disilab.ar/public/api/desastres", this.importadorAPI,
                TipoFuente.PROXY);
        hechosImportados = this.importadorAPI.importar(fuente);
        Coleccion coleccion = new Coleccion(
                UUID.randomUUID().toString(),
                "COLECCION API",
                "Colección creada a partir de datos de API",
                new ArrayList<>());
        if (hechosImportados != null) {
            for (Hecho hecho : hechosImportados) {
                if (hecho != null) {
                    coleccion.setHecho(hecho);
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

    @Override
    public List<Hecho> navegarHechos(String coleccionId, ModoNavegacionStrategy modoNavegacion) {

        Coleccion coleccion = coleccionRepository.findById(coleccionId);
        List<Hecho> hechos = this.obtenerHechosPorColeccion(coleccionId);
        List<Hecho> hechosFiltrados = modoNavegacion.filtrar(hechos);
        return hechosFiltrados;
    }

    public void setAlgoritmoDeConsenso(String idColeccion, AlgoritmoDeConsensoStrategy algoritmo) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        coleccion.setAlgoritmoDeConsenso(algoritmo);
        coleccionRepository.save(coleccion);
    }


    public Coleccion setColeccionCsv() {
        List<Hecho> hechosImportadosCSV;
        Fuente fuente = new Fuente("C:/Users/Usuario/Desktop/DSI/2025-tpa-mi-no-grupo-15/src/main/java/ar/edu/utn/frba/Assets/prueba1.csv/", this.importador, TipoFuente.LOCAL);
        hechosImportadosCSV = this.importadorCSV.importar("C:/Users/Usuario/Desktop/DSI/2025-tpa-mi-no-grupo-15/src/main/java/ar/edu/utn/frba/Assets/prueba1.csv/");
        Coleccion coleccionCSV = new Coleccion(
                UUID.randomUUID().toString(),
                "COLECCION CSV",
                "Colección creada a partir de datos de CSV",
                new ArrayList<>()
        );
        coleccionCSV.setHechos(hechosImportadosCSV);
        coleccionRepository.save(coleccionCSV);
        return coleccionCSV;
    }
}
