package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import ar.edu.utn.frba.server.servicioAgregador.domain.Etiqueta;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data @Builder
public class HechosInputDto {
    private String titulo;
    private String descripcion;
    private String categoria;
    private ContenidoMultimediaDto contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    @Builder.Default private List<Etiqueta> etiquetas = new ArrayList<>();
    private ContribuyenteDto contribuyente;
}
