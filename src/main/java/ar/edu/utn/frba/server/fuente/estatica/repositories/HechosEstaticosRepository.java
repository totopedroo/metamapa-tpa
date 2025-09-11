package ar.edu.utn.frba.server.fuente.estatica.repositories;

import ar.edu.utn.frba.server.fuente.estatica.domain.Hecho;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("hechosEstaticosRepository")
public class HechosEstaticosRepository implements IHechosEstaticosRepository {

    private final List<Hecho> hechos = new ArrayList<>();

    @Override public void save(Hecho hecho) { hechos.add(hecho); }

    @Override public List<Hecho> findAll() { return hechos; }

    @Override
    public List<Hecho> buscarNoSincronizados() {
        return hechos.stream()
                .filter(h -> !h.isEstaSincronizado())
                .toList();
    }
}
