package ar.edu.utn.frba.server.fuente.estatica.repositories;

import ar.edu.utn.frba.server.fuente.estatica.domain.Hecho;

import java.util.List;

public interface IHechosEstaticosRepository {

    public void save(Hecho hecho);

    public List<Hecho> findAll();

    public List<Hecho> buscarNoSincronizados();
}
