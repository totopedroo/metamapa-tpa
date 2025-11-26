package ar.edu.utn.frba.server.gestorUsuarios.dtos;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Rol;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserRolesPermissionsDTO {
    private Long id;
    private String username;
    private Rol rol;
    private List<String> permisos;
}