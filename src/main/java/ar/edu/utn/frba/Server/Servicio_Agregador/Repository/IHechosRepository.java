package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;

import java.util.List;

public interface IHechosRepository {

    public List<Hecho> findAll();
    public Hecho save(Hecho hecho);
    public void delete(Hecho hecho);
    public Hecho findById(long id);
    public void saveList(List<Hecho> listaHechos);
    public Hecho create(Hecho hecho);
}