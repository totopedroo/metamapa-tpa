package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Etiqueta;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AgregadorMapper {
    private static final SecureRandom RNG = new SecureRandom();


    private ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta castear(Etiqueta etiquetaACastear){
        return new ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta(etiquetaACastear.getEtiqueta());
    }

    public Hecho toDomain(HechoDto d) {
        List<ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta> etiquetas_cast= new ArrayList<>();
        etiquetas_cast=d.etiquetas().stream().map(e -> castear(e)).collect(Collectors.toList());
        Hecho h = Hecho.builder()
                .idHecho(d.id() != null ? d.id() : (RNG.nextLong() >>> 1)) // id positivo
                .titulo(d.titulo())
                .descripcion(d.descripcion())
                .categoria(d.categoria())
                .latitud(d.latitud())
                .longitud(d.longitud())
                .fechaAcontecimiento(d.fecha() != null ? d.fecha() : LocalDate.now())
                .etiquetas(d.etiquetas() == null ? List.of() : etiquetas_cast)
                .build();

        // origen por nombre de fuente
        String f = d.fuente() == null ? "" : d.fuente().toLowerCase();
        TipoFuente origen =
                f.startsWith("proxy") ? TipoFuente.PROXY :
                        f.contains("estatica") ? TipoFuente.ESTATICA :
                                TipoFuente.DINAMICA;

        //h.setFuente(new Fuente("",origen));
        return h;
    }
}
