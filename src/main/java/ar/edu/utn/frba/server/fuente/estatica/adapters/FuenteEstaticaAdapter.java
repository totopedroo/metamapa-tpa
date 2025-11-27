package ar.edu.utn.frba.server.fuente.estatica.adapters;

import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import ar.edu.utn.frba.server.contratos.fuentes.FuentePort;
import ar.edu.utn.frba.server.fuente.estatica.domain.ImportadorCSV;
import ar.edu.utn.frba.server.fuente.estatica.mappers.EstaticaMapper;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FuenteEstaticaAdapter implements FuentePort {

    private final ImportadorCSV csv;
    private final EstaticaMapper mapper;
    private final String rutaCsv;

    public FuenteEstaticaAdapter(
            ImportadorCSV csv,
            EstaticaMapper mapper,
            @Value("${fuente.estatica.csv.path}") String rutaCsv) {
        this.csv = csv;
        this.mapper = mapper;
        this.rutaCsv = rutaCsv;
    }

    @Override
    public List<HechoDto> importar(String criterio) {
        List<Hecho> hechos = csv.importar(rutaCsv);
        return hechos.stream()
                .filter(h -> match(h, criterio))
                .map(mapper::toHechoDto)
                .toList();
    }

    private boolean match(Hecho h, String criterio) {
        if (criterio == null || criterio.isBlank()) return true;
        String c = criterio.toLowerCase();
        return (h.getTitulo() != null && h.getTitulo().toLowerCase().contains(c))
                || (h.getDescripcion() != null && h.getDescripcion().toLowerCase().contains(c))
                || (h.getCategoria() != null && h.getCategoria().toLowerCase().contains(c))
              ;
    }
}
