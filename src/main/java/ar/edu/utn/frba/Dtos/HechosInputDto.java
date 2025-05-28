package ar.edu.utn.frba.Dtos;

import ar.edu.utn.frba.domain.ContenidoMultimedia;
import ar.edu.utn.frba.domain.Contribuyente;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Getter
@Setter
@Data
public class HechosInputDto {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Optional<ContenidoMultimedia> contenidoMultimedia = Optional.empty();
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas = new ArrayList();
    private Contribuyente contribuyente;
}
