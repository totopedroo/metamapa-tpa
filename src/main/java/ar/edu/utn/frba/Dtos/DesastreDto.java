package ar.edu.utn.frba.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DesastreDto {
    private long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private double latitud;
    private double longitud;
    @JsonProperty("fecha_hecho")
    private LocalDate fecha;

}
