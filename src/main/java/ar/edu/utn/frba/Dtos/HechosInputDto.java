package ar.edu.utn.frba.Dtos;

import ar.edu.utn.frba.domain.ContenidoMultimedia;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Data
public class HechosInputDto {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Optional<ContenidoMultimedia> contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas = new ArrayList();
}
