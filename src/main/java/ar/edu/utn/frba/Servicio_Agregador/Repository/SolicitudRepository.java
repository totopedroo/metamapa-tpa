package ar.edu.utn.frba.Servicio_Agregador.Repository;

import ar.edu.utn.frba.domain.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SolicitudRepository implements ISolicitudRepository {

    private List<SolicitudEliminacion> solicitudes = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public void guardarSolicitud(SolicitudEliminacion solicitud) {
        solicitud.setIdSolicitud(nextId++);
        solicitudes.add(solicitud);
    }

    @Override
    public List<SolicitudEliminacion> findAll() {
        return solicitudes;
    }
}
