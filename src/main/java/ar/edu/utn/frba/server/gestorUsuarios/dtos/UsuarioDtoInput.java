package ar.edu.utn.frba.server.gestorUsuarios.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioDtoInput {
    @NotBlank
    private String nombre;

    private String apellido;

    @NotBlank
    private String mail;

    @NotBlank
    private String password;

}