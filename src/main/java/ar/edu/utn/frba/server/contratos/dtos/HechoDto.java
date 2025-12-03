package ar.edu.utn.frba.server.contratos.dtos;

import java.time.LocalDate;

public record HechoDto(
        Long id,
        String titulo,
        String descripcion,
        String fuente,
        String categoria,
        Double latitud,
        Double longitud,
        LocalDate fecha         // fecha del acontecimiento
) {}
