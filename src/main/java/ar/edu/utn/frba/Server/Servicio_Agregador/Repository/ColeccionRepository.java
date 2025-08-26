package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.ColeccionInputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.domain.main;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("coleccionRepository")
public class ColeccionRepository implements IColeccionRepository {

    private final main.ImportadorAPI importadorAPI;
    public List<Coleccion> colecciones;
    public ColeccionInputDto coleccionInput;

    public ColeccionRepository(main.ImportadorAPI importadorAPI) {
        this.colecciones = new ArrayList<>();
        this.importadorAPI = importadorAPI;
    }

    public Coleccion create(ColeccionInputDto coleccionInputDto) {
        if (coleccionInputDto == null) {
            throw new IllegalArgumentException("no puede ser null");
        }
        String newId = UUID.randomUUID().toString();
        Coleccion nuevaColeccion = new Coleccion(
                newId,
                coleccionInputDto.getTitulo(),
                coleccionInputDto.getDescripcion(),
                coleccionInputDto.getCriterioDePertenencia() != null ? coleccionInputDto.getCriterioDePertenencia()
                        : new ArrayList<>());
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
        return this.colecciones.stream().filter(g -> g.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Coleccion> findAll() {
        // setColeccionEstatica(); //para pruebas
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
