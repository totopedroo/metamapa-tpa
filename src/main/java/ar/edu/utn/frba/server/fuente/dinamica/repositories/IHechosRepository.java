package ar.edu.utn.frba.server.fuente.dinamica.repositories;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import java.util.List;

public interface IHechosRepository {
    Hecho findById(long id);
    List<Hecho> findAll();
    void save(Hecho h);
    void delete(Hecho h);
}
