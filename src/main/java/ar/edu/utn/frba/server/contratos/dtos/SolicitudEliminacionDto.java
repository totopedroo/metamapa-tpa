package ar.edu.utn.frba.server.contratos.dtos;

public record SolicitudEliminacionDto(
        Long idHecho,
        String motivo,
        String solicitante,
        Long id
) {}