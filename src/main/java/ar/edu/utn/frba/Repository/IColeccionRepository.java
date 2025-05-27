package ar.edu.utn.frba.Repository;

import ar.edu.utn.frba.Dtos.ColeccionInputDto;
import ar.edu.utn.frba.domain.Coleccion;

import java.util.List;

public interface IColeccionRepository {
    public List<Coleccion> findAll();
    public void save(Coleccion coleccion);
    public void delete(Coleccion coleccion);
    public Coleccion findById(String id);
    Coleccion create(ColeccionInputDto coleccionInputDto); // Updated signature
}
