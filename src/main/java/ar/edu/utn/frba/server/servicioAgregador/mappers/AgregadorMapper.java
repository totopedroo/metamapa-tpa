package ar.edu.utn.frba.server.servicioAgregador.mappers;

import ar.edu.utn.frba.server.common.dtos.HechoDto;
import ar.edu.utn.frba.server.common.enums.EstadoRevisionHecho;
import ar.edu.utn.frba.server.common.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AgregadorMapper {

    // Instancias compartidas de Fuente para evitar duplicados y loops
    private static final Fuente FUENTE_PROXY = new Fuente(TipoFuente.PROXY);
    private static final Fuente FUENTE_ESTATICA = new Fuente(TipoFuente.ESTATICA);
    private static final Fuente FUENTE_DINAMICA = new Fuente(TipoFuente.DINAMICA);

    private Etiqueta castear(Etiqueta etiquetaACastear) {
        return new Etiqueta(etiquetaACastear.getEtiqueta());
    }

    public Hecho toDomain(HechoDto d) {
        Hecho h = Hecho.builder()
                .titulo(d.titulo())
                .descripcion(d.descripcion())
                .categoria(d.categoria())
                .latitud(d.latitud())
                .longitud(d.longitud())
                .fechaAcontecimiento(d.fecha() != null ? d.fecha() : LocalDate.now())
                .fechaCarga(LocalDate.now())
                .build();

        // Detectar tipo de fuente desde el DTO
        String f = d.fuente() == null ? "" : d.fuente().toLowerCase();
        TipoFuente origen =
                f.startsWith("proxy") ? TipoFuente.PROXY :
                        f.contains("estatica") ? TipoFuente.ESTATICA :
                                TipoFuente.DINAMICA;

        Fuente fuente = switch (origen) {
            case PROXY -> FUENTE_PROXY;
            case ESTATICA -> FUENTE_ESTATICA;
            case DINAMICA -> FUENTE_DINAMICA;
        };

        // Asegurar lista mutable y evitar duplicados
        h.setFuentes(new ArrayList<>(List.of(fuente)));

        // Estado de revisión automático
        if (origen == TipoFuente.DINAMICA) {
            h.setEstadoRevision(EstadoRevisionHecho.PENDIENTE);
        } else {
            h.setEstadoRevision(EstadoRevisionHecho.APROBADO);
        }

        return h;
    }
}
