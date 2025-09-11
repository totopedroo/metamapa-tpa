package ar.edu.utn.frba.server.contratos.fuentes;

import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import java.util.List;

public interface FuentePort {
    List<HechoDto> importar(String criterio);
}

