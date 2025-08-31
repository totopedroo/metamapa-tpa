package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;

import java.util.List;
import java.util.Optional;

public interface IHechosRepository {

    public List<Hecho> findAll();
    public Hecho save(Hecho hecho);
    public void delete(Hecho hecho);
    public Hecho findById(Long id);
    public void saveList(List<Hecho> listaHechos);
    public Hecho create(Hecho hecho);
}