package ar.edu.utn.frba.server.fuente.dinamica.repositories;

import ar.edu.utn.frba.server.fuente.dinamica.domain.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository("solicitudDinamicaRepository")
public class SolicitudRepository implements ISolicitudRepository {

    private final Map<Long, SolicitudEliminacion> store = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(1L);

    @Override
    public SolicitudEliminacion save(SolicitudEliminacion s) {
        if (s.getIdSolicitud() == 0L) {
            s.setId(seq.getAndIncrement());
        }
        store.put(s.getIdSolicitud(), s);
        return s;
    }

    @Override
    public Optional<SolicitudEliminacion> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<SolicitudEliminacion> findByHecho(Long idHecho) {
        return store.values().stream()
                .filter(x -> x.getIdHechoAsociado() == idHecho)
                .toList();
    }

    @Override
    public List<SolicitudEliminacion> findAll() {
        return List.copyOf(store.values()); // copia inmutable
    }
}
