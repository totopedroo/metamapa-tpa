package ar.edu.utn.frba.server.fuente.dinamica.repositories;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository("hechosDinamicaRepository")
public class HechosRepository implements IHechosRepository {

    private final List<Hecho> store = new ArrayList<>();

    public HechosRepository() {}

    @Override
    public synchronized Hecho findById(long id) {
        return store.stream()
                .filter(h -> Objects.equals(h.getIdHecho(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized List<Hecho> findAll() {
        return new ArrayList<>(store); // copia defensiva
    }

    /** Alta o upsert simple (si ya existe por id, lo reemplaza). */
    @Override
    public synchronized void save(Hecho hecho) {
        if (hecho == null) throw new IllegalArgumentException("El hecho no puede ser nulo");

        // si no trae id, generá uno (opcional, si tu modelo lo permite)
        if (hecho.getIdHecho() == null) {
            long nextId = store.stream()
                    .map(Hecho::getIdHecho)
                    .filter(Objects::nonNull)
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0L) + 1L;
            hecho.setIdHecho(nextId);
        }

        // upsert
        for (int i = 0; i < store.size(); i++) {
            Hecho h = store.get(i);
            if (Objects.equals(h.getIdHecho(), hecho.getIdHecho())) {
                store.set(i, hecho);
                return;
            }
        }
        store.add(hecho);
    }

    public synchronized void saveList(List<Hecho> listaHechos) {
        if (listaHechos == null) return;
        for (Hecho h : listaHechos) save(h);
    }

    @Override
    public synchronized void delete(Hecho hecho) {
        if (hecho == null) return;
        store.removeIf(h -> Objects.equals(h.getIdHecho(), hecho.getIdHecho()));
    }
}
