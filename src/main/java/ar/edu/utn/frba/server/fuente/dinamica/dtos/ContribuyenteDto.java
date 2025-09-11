package ar.edu.utn.frba.server.fuente.dinamica.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContribuyenteDto {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}

