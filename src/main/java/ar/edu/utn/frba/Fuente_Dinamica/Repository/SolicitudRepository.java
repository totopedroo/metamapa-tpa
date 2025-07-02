package ar.edu.utn.frba.Fuente_Dinamica.Repository;

import ar.edu.utn.frba.Fuente_Dinamica.Repository.ISolicitudRepository;
import ar.edu.utn.frba.Fuente_Dinamica.Domain.SolicitudEliminacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("solicitudDinamicaRepository")
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
