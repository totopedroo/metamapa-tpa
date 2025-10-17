package ar.edu.utn.frba.server.gestorUsuarios.dtos;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Rol;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
public class UsuarioDtoInput {
    private String nombre;
    private String email;
    @NotBlank(message = "El username es obligatorio")
    private String username;
    @NotBlank(message = "El password es obligatorio")
    private String password;
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ADMIN|CONTRIBUYENTE|VISUALIZADOR",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "El rol debe ser ADMIN, CONTRIBUYENTE o VISUALIZADOR")
    private String rol;

}