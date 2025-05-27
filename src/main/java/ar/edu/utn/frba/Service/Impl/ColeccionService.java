package ar.edu.utn.frba.Service.Impl;

import java.util.List;
import ar.edu.utn.frba.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.Repository.IColeccionRepository;
import ar.edu.utn.frba.Service.IColeccionService;
import ar.edu.utn.frba.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frba.Dtos.HechosOutputDto;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    @Autowired
    private IColeccionRepository coleccionRepository;

    @Autowired
    private ImportadorAPI importadorAPI;

    @Autowired
    private ImportadorCSV importadorCSV;

    @Override
    public List<ColeccionOutputDto> buscarTodos() {
        List<Coleccion> colecciones = coleccionRepository.findAll();
        return colecciones.stream().map(this::coleccionOutputDto).toList();
    }

    public ColeccionOutputDto coleccionOutputDto(Coleccion coleccion) {
        var dto = new ColeccionOutputDto();
        dto.setId(coleccion.getId());
        dto.setHechos(coleccion.getHechos()); // Assumes getHechos() returns the desired list
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
        return dto;
    }

    public List<HechosOutputDto> obtenerHechosPorColeccion(String idColeccion) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + idColeccion);
        }
        return coleccion.getHechos().stream()
                .map(HechosOutputDto::fromModel)
                .collect(Collectors.toList());
    }

    public Coleccion setColeccionApi() {

        List<Hecho> hechosImportados;

        Fuente fuente = new Fuente("https://api-ddsi.disilab.ar/public/api/desastres", this.importadorAPI, TipoFuente.PROXY);
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
                }
            }
        }
        coleccionRepository.save(coleccion);
        return coleccion;
    }


 public Coleccion setColeccionCsv() {
     List<Hecho> hechosImportadosCSV;
     Fuente fuente = new Fuente("C:/Users/Usuario/Desktop/DSI/2025-tpa-mi-no-grupo-15/prueba1.csv", this.importadorCSV, TipoFuente.LOCAL);
     hechosImportadosCSV = this.importadorCSV.importar(fuente);
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

    public void actualizarHechos(List<Hecho> nuevosHechos) {
        for (Coleccion coleccion : coleccionRepository.findAll()) {
            for (Hecho hecho : nuevosHechos) {
                if (hecho.estaEliminado()) continue;

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

}
