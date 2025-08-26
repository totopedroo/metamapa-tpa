package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.ColeccionInputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;

import java.util.List;

public interface IColeccionRepository {
    public List<Coleccion> findAll();
    public void save(Coleccion coleccion);
    public void delete(Coleccion coleccion);
    public Coleccion findById(String id);
    Coleccion create(ColeccionInputDto coleccionInputDto); 

}

