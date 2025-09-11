package ar.edu.utn.frba.server.contratos.dtos;

import java.util.List;

public record ColeccionDto(
        String id,
        String titulo,
        String descripcion,
        List<HechoDto> hechos
) {}