package ar.edu.utn.frba.server.servicioAgregador.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class HechoFrontDto {

    private Long idHecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String contenidoMultimedia;
    private LocalDate fechaAcontecimiento;
    private LocalTime horaAcontecimiento;
    private LocalDate fechaCarga;
    private Double latitud;
    private Double longitud;
    private String provincia;
    private List<String> etiquetas;
    private List<String> consensos;
    private Boolean consensuado;
    private List<String> fuentes;
    private Long idContribuyente;
}