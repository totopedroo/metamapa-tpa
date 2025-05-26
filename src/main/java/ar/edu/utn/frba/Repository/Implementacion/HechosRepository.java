package ar.edu.utn.frba.Repository.Implementacion;

import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.domain.Hecho;

import java.util.ArrayList;
import java.util.List;

public class HechosRepository implements IHechosRepository {
    private final List<Hecho> hechos = new ArrayList<>();

    @Override
    public void guardarHecho(Hecho hecho) {
        hechos.add(hecho);
    }

    @Override
    public List<Hecho> findAll() {
        return new ArrayList<>(hechos);
    }

    @Override
    public Hecho findById(long id) {
        return this.hechos.stream().
                filter(g -> g.getIdHecho() == id).findFirst().
                orElse(null);
    }

    @Override
    public void borrarHecho(Hecho hecho) {
        hechos.remove(hecho);
    }
}
