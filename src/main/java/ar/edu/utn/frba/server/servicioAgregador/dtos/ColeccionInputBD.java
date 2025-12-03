package ar.edu.utn.frba.server.servicioAgregador.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor // <--- AÑADIR ESTA ANOTACIÓN
public class ColeccionInputBD {
    @NotBlank(message = "titulo es obligatorio")
    private String titulo;
    private String descripcion;
    @NotNull(message = "administradorId es obligatorio")
    private Long administradorId;

    // Enlaces opcionales por ID a objetos ya existentes
    private List<Long> hechosIds = List.of();
    private List<Long> criteriosIds = List.of();
}

