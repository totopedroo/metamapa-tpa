package ar.edu.utn.frba.server.contratos.dtos;

import java.time.LocalDate;
import java.util.List;

public record HechoDto(
        Long id,
        String titulo,
        String descripcion,
        String fuente,
        String categoria,
        Double latitud,
        Double longitud,
        LocalDate fecha,         // fecha del acontecimiento
        List<String> etiquetas
) {}
