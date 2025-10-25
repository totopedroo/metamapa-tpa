package ar.edu.utn.frba.server.servicioAgregador.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ColeccionOutputBD {
    private Long id;
    private String titulo;
    private String descripcion;
    private Long administradorId;
    private List<Long> hechosIds;
    private List<Long> criteriosIds;
}