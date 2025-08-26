package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("solicitudAgregadorRepository")
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
