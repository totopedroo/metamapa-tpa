package Dtos;

import domain.ContenidoMultimedia;
import domain.SolicitudDeEliminacion;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Data
public class HechosOutputDto {
    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Optional<ContenidoMultimedia> contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private List<String> etiquetas = new ArrayList();
    private List <SolicitudDeEliminacion> solicitudes= new ArrayList();
}
