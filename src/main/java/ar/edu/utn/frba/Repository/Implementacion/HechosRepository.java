package ar.edu.utn.frba.Repository.Implementacion;

import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {

    public List<Hecho> hechos;

    public HechosRepository() {
        this.hechos = new ArrayList<>();
    }

    @Override
    public Hecho findById(long id) {
        System.out.println("hechos: "+ hechos);
        return this.hechos.stream().
                filter(g -> g.getIdHecho() == id).findFirst().
                orElse(null);
    }

    @Override
    public List<Hecho> findAll() {
        System.out.println("hechos: "+ hechos);
        return hechos;
    }

    @Override
    public void guardarHecho(Hecho hecho) {
        hechos.add(hecho);
    }

    @Override
    public void borrarHecho(Hecho hecho) {
        hechos.remove(hecho);
    }
}
