package ar.edu.utn.frba.Fuente_Proxy.Repository;

import ar.edu.utn.frba.Fuente_Proxy.Domain.Hecho;

import java.util.List;

public interface IHechosRepository {
    public List<Hecho> findAll();
    public void save(Hecho hecho);
    public void delete(Hecho hecho);
    public Hecho findById(long id);
    public void saveList(List<Hecho> listaHechos);
    public Hecho create(Hecho hecho);
}