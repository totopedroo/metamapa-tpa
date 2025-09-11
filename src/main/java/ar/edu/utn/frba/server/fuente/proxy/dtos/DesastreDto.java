package ar.edu.utn.frba.server.fuente.proxy.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
// import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesastreDto {
    private Long id;
    private String titulo, descripcion, categoria;
    private Double latitud, longitud;
        @JsonProperty("fecha_hecho")  private OffsetDateTime fechaHecho;
}
