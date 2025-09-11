package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;

import java.util.List;

public interface IHechosRepository {

    public List<Hecho> findAll();
    public Hecho save(Hecho hecho);
    public void delete(Hecho hecho);
    public Hecho findById(Long id);
    public void saveList(List<Hecho> listaHechos);
    public Hecho create(Hecho hecho);
}