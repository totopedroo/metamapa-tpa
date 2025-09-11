package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContenidoMultimediaDto {
    private String url;
}
