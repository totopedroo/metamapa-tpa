package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;

import java.util.List;

public interface IColeccionRepository {
    public List<Coleccion> findAll();
    public Coleccion save(Coleccion coleccion);
    public void delete(Coleccion coleccion);
    public Coleccion findById(String id);
    Coleccion create(Coleccion coleccion);

}

