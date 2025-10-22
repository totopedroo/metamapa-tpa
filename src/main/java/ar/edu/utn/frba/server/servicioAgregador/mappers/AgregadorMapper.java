package ar.edu.utn.frba.server.servicioAgregador.mappers;

import ar.edu.utn.frba.server.common.dtos.HechoDto;
import ar.edu.utn.frba.server.common.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AgregadorMapper {
    private static final SecureRandom RNG = new SecureRandom();


    private Etiqueta castear(Etiqueta etiquetaACastear){
        return new Etiqueta(etiquetaACastear.getEtiqueta());
    }

    public Hecho toDomain(HechoDto d) {
        List<Etiqueta> etiquetas_cast = new ArrayList<>();

        Hecho h = Hecho.builder()
                .idHecho(d.id() != null ? d.id() : (RNG.nextLong() >>> 1)) // id positivo
                .titulo(d.titulo())
                .descripcion(d.descripcion())
                .categoria(d.categoria())
                .latitud(d.latitud())
                .longitud(d.longitud())
                .fechaAcontecimiento(d.fecha() != null ? d.fecha() : LocalDate.now())
                .build();

        // Detectar fuente por nombre
        String f = d.fuente() == null ? "" : d.fuente().toLowerCase();
        TipoFuente origen =
                f.startsWith("proxy") ? TipoFuente.PROXY :
                        f.contains("estatica") ? TipoFuente.ESTATICA :
                                TipoFuente.DINAMICA;

        // Asignar fuente (lista, porque el Hecho lo usa así)
        h.setFuente(List.of(new Fuente(origen)));

        // Estado de revisión automático
        if (origen == TipoFuente.DINAMICA) {
            h.setEstadoRevision(ar.edu.utn.frba.server.common.enums.EstadoRevisionHecho.PENDIENTE);
        } else {
            h.setEstadoRevision(ar.edu.utn.frba.server.common.enums.EstadoRevisionHecho.APROBADO);
        }

        return h;
    }

}
