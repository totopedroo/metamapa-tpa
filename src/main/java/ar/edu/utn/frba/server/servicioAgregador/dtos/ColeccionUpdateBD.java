package ar.edu.utn.frba.server.servicioAgregador.dtos;

import java.util.List;

public record ColeccionUpdateBD(
        String titulo,
        String descripcion,
        Long administradorId,
        List<Long> hechosIds,
        List<Long> criteriosIds
) {}