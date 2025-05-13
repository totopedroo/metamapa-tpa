package Repository.Implementacion;

import Repository.IHechosRepository;
import domain.Hecho;

import java.util.ArrayList;
import java.util.List;

public class HechosRepository implements IHechosRepository {
    private List<Hecho> hechos;
    public HechosRepository() {
        this.hechos= new ArrayList<>();}

    @Override
    public Hecho findById(long id) {
        return this.hechos.stream().
                filter(g ->g.getIdHecho().equals(id)).findFirst().
                orElse(null);
    }

    @Override
    public List<Hecho> findAll() {
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
