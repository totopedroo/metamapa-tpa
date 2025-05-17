package ar.edu.utn.frba.Repository;

import ar.edu.utn.frba.domain.Hecho;

import java.util.List;

public interface IHechosRepository {
    public List<Hecho> findAll();
    public void guardarHecho(Hecho hecho);
    public void borrarHecho(Hecho hecho);
    public Hecho findById(long id);
}
