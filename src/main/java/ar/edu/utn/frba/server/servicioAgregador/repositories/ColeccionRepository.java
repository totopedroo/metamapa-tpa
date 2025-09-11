package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("coleccionRepository")
public class ColeccionRepository implements IColeccionRepository {

    private final List<Coleccion> colecciones = new ArrayList<>();

    @Override
    public Coleccion findById(String id) {
        return colecciones.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Coleccion> findAll() {
        return new ArrayList<>(colecciones); // copia defensiva
    }

    /** Alta / upsert */
    @Override
    public Coleccion save(Coleccion coleccion) {
        colecciones.removeIf(c -> c.getId().equals(coleccion.getId()));
        colecciones.add(coleccion);
        return coleccion;
    }

    @Override
    public void delete(Coleccion coleccion) {
        colecciones.remove(coleccion);
    }

    /** Opcional si querés mantener create en el repo, pero recibiendo dominio */
    @Override
    public Coleccion create(Coleccion coleccion) {
        return save(coleccion);
    }
}
