package ar.edu.utn.frba.server.servicioUsuarios.dtos;

import ar.edu.utn.frba.server.servicioUsuarios.domain.Permiso;
import ar.edu.utn.frba.server.servicioUsuarios.domain.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesPermissionsDTO {
    private String username;
    private Rol rol;
    private List<Permiso> permisos;
}
