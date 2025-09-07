package ar.edu.utn.frba.Server.Servicio_Agregador.Dtos;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.ContenidoMultimedia;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Contribuyente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.TipoFuente;
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
    private ContenidoMultimedia contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaAcontecimiento;
    private List<String> etiquetas = new ArrayList();
    private Contribuyente contribuyente;
    private boolean eliminado = false;
    private Fuente fuente;
}
