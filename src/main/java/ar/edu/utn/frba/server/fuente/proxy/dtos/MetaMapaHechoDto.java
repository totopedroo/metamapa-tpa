package ar.edu.utn.frba.server.fuente.proxy.dtos;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Etiqueta;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class MetaMapaHechoDto {
    private Long id;
    private String titulo, descripcion, categoria;
    private Double latitud, longitud;
    @JsonAlias({"fecha", "fecha_acontecimiento"})
    private String fechaAcontecimiento;
    //Opcional si las trae.
    private List<Etiqueta> etiquetas;
}

