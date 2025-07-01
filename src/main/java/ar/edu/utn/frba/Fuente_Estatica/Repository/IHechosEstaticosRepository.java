package ar.edu.utn.frba.Fuente_Estatica.Repository;

import ar.edu.utn.frba.Fuente_Estatica.Domain.Hecho;

import java.util.List;

public interface IHechosEstaticosRepository {

    public void save(Hecho hecho);

    public List<Hecho> findAll();

    public List<Hecho> buscarNoSicronizados();
}
