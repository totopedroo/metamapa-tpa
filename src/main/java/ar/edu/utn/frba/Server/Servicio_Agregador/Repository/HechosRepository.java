package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;


import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository("hechosRepository")   // nombre explícito del bean
public class HechosRepository {
    private final List<Hecho> hechos = new ArrayList<>();
    private final AtomicLong sec = new AtomicLong(1);


    public Hecho save(Hecho h) {
        if (h.getIdHecho() == null) {
            h.setIdHecho(sec.getAndIncrement());
        }
// reemplazar si ya existe
        hechos.removeIf(x -> Objects.equals(x.getIdHecho(), h.getIdHecho()));
        hechos.add(h);
        return h;
    }

   
    public void delete(Hecho hecho) {

    }


    public Hecho findById(long id) {
        return null;
    }


    public void saveList(List<Hecho> listaHechos) {

    }


    public Hecho create(Hecho hecho) {
        return null;
    }


    public Optional<Hecho> findById(Long id) {
        return hechos.stream().filter(h -> Objects.equals(h.getIdHecho(), id)).findFirst();
    }


    public List<Hecho> findAll() { return new ArrayList<>(hechos); }
}