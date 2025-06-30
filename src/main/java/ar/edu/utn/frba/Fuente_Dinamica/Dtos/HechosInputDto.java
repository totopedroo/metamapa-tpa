package ar.edu.utn.frba.Fuente_Dinamica.Dtos;

import ar.edu.utn.frba.Fuente_Dinamica.Domain.ContenidoMultimedia;
import ar.edu.utn.frba.Fuente_Dinamica.Domain.Contribuyente;
import ar.edu.utn.frba.Fuente_Dinamica.Domain.Fuente;
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
    private LocalDate fechaCarga;
    private List<String> etiquetas = new ArrayList();
    private Contribuyente contribuyente;
    private Long IdHecho;
    private boolean eliminado = false;
    private Fuente fuente;
}
