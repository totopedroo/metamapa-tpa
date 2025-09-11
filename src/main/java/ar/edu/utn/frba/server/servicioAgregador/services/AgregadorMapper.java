package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Component
public class AgregadorMapper {
    private static final SecureRandom RNG = new SecureRandom();

    public Hecho toDomain(HechoDto d) {
        Hecho h = Hecho.builder()
                .idHecho(d.id() != null ? d.id() : (RNG.nextLong() >>> 1)) // id positivo
                .titulo(d.titulo())
                .descripcion(d.descripcion())
                .categoria(d.categoria())
                .latitud(d.latitud())
                .longitud(d.longitud())
                .fechaAcontecimiento(d.fecha() != null ? d.fecha() : LocalDate.now())
                .etiquetas(d.etiquetas() == null ? List.of() : d.etiquetas())
                .build();

        // origen por nombre de fuente
        String f = d.fuente() == null ? "" : d.fuente().toLowerCase();
        TipoFuente origen =
                f.startsWith("proxy") ? TipoFuente.PROXY :
                        f.contains("estatica") ? TipoFuente.LOCAL :
                                TipoFuente.DINAMICA;

        h.setTipoFuente(origen);
        return h;
    }
}
