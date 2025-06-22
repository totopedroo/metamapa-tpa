package ar.edu.utn.frba.Fuente_Estatica.Repository;

import ar.edu.utn.frba.Fuente_Estatica.Domain.HechoEstatico;

import java.util.List;

public interface IHechosEstaticosRepository {

    public void save(HechoEstatico hecho);

    public List<HechoEstatico> findAll();

    public List<HechoEstatico> buscarNoSicronizados();
}
