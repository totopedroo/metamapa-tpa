package ar.edu.utn.frba.Repository.Implementacion;

import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.domain.Coleccion;
import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.ImportadorAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class HechosRepository implements IHechosRepository {

    public List<Hecho> hechosApi;
    public Fuente fuente;
    public ImportadorAPI importador;
    public List<Hecho> hechos;



 /*   @Override
    public List<Hecho> findAll() {
        return new ArrayList<>(hechos);
    }
*/
    @Override
    public Hecho findById(long id) {
        System.out.println("hechos: " + hechos);
        return this.hechos.stream().
                filter(g -> g.getIdHecho() == id).findFirst().
                orElse(null);
    }

    @Override

    public List<Hecho> findAll() {
        System.out.println("hechos: " + hechos);
        return hechos;
    }

    @Override
    public void save(Hecho hecho) {
        hechos.add(hecho);
    }


    public void saveList(List<Hecho> listaHechos) {
        hechos.addAll(listaHechos);
    }

    @Override
    public void delete(Hecho hecho) {
        hechos.remove(hecho);
    }
}
