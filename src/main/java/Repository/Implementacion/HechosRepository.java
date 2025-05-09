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
    public List<Hecho> findAll() {
        return List.of();
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
