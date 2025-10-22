package ar.edu.utn.frba.server.common.dtos;

public record SolicitudEliminacionDto(
        Long idHecho,
        String motivo,
        String solicitante,
        Long id
) {}