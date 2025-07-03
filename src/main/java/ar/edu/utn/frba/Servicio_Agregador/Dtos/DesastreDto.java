package ar.edu.utn.frba.Servicio_Agregador.Dtos;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.TipoFuente;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Proxy;
import java.time.LocalDate;

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
    private TipoFuente tipo = TipoFuente.PROXY;


    public TipoFuente getTipo() {
        return tipo;
    }
}
