package ar.edu.utn.frba.server.fuente.domain;

import ar.edu.utn.frba.server.common.dtos.HechoDto;
import java.util.List;

public interface FuentePort {
    List<HechoDto> importar(String criterio);
}

