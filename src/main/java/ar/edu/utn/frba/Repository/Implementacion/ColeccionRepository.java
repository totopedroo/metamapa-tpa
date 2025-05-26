package ar.edu.utn.frba.Repository.Implementacion;

import ar.edu.utn.frba.Dtos.ColeccionInputDto;
import ar.edu.utn.frba.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Repository.IColeccionRepository;
import ar.edu.utn.frba.domain.*;
import ar.edu.utn.frba.domain.Coleccion;
import ar.edu.utn.frba.Dtos.SolicitudOutputDto;
import ar.edu.utn.frba.Dtos.SolicitudInputDto;

import java.util.List;


import ar.edu.utn.frba.domain.Coleccion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;


@Repository
public class ColeccionRepository implements IColeccionRepository {

    private final ImportadorAPI importadorAPI;
    public List<Coleccion> colecciones;
    public ColeccionInputDto coleccionInput;

    public ColeccionRepository(ImportadorAPI importadorAPI) {
        this.colecciones = new ArrayList<>();
        this.importadorAPI = importadorAPI;
    }

    public Coleccion create(ColeccionInputDto coleccionInputDto){
        if (coleccionInputDto == null) {
            throw new IllegalArgumentException("no puede ser null");
        }
        String newId = UUID.randomUUID().toString();
        Coleccion nuevaColeccion = new Coleccion(
                newId,
                coleccionInputDto.getTitulo(),
                coleccionInputDto.getDescripcion(),
                coleccionInputDto.getCriterioDePertenencia() != null ? coleccionInputDto.getCriterioDePertenencia() : new ArrayList<>()
        );
        if (coleccionInputDto.getHechos() != null) {
            for (Hecho hecho : coleccionInputDto.getHechos()) {

                nuevaColeccion.setHecho(hecho);
            }
        }
        this.colecciones.add(nuevaColeccion);
        return nuevaColeccion;
    }

    @Override
    public Coleccion findById(String id) {
        return this.colecciones.stream().
                filter(g ->g.getId().equals(id)).findFirst().
                orElse(null);
    }

    /* public void setColeccionEstatica(){
        Importador importador = new Importador();
        importador.importarFromCSV("prueba1.csv");
        Coleccion coleccionPrueba = new Coleccion("1", "Coleccionde prueba estatica", "Coleccion para pruebas, es de tipo estatica, se lee de un archivo csv", null);
        List<Hecho> hechos = new ArrayList<Hecho>();
        hechos = importador.getHechos();
        coleccionPrueba.setHechos(hechos);
        save(coleccionPrueba);
    }//para pruebas
*/


    @Override
    public List<Coleccion> findAll() {
        //setColeccionEstatica(); //para pruebas
        return colecciones;
    }

    @Override
    public void save(Coleccion coleccion) {
        colecciones.add(coleccion);
    }

    @Override
    public void delete(Coleccion coleccion) {
        colecciones.remove(coleccion);
    }


}