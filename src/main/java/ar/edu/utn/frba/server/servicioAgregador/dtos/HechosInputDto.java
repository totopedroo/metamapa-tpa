package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.servicioAgregador.domain.ContenidoMultimedia;
import ar.edu.utn.frba.server.servicioAgregador.domain.Contribuyente;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
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
    private boolean eliminado = false;
    private TipoFuente fuente;
}
