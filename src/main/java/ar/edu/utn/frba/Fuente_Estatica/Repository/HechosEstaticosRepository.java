package ar.edu.utn.frba.Fuente_Estatica.Repository;

import ar.edu.utn.frba.Fuente_Estatica.Domain.Hecho;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("hechosEstaticosRepository")
public class HechosEstaticosRepository implements IHechosEstaticosRepository {

    private List<Hecho> hechos;

    public HechosEstaticosRepository() {
        hechos = new ArrayList<Hecho>();
    }

    @Override
    public void save(Hecho hecho) {
        hechos.add(hecho);
    }

    @Override
    public List<Hecho> findAll() {
        return hechos;
    }

    @Override
    public List<Hecho> buscarNoSicronizados() {
        return hechos.stream().filter(h -> h.getEstaSincronizado() == false).toList();
    }
}
