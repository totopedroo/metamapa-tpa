package ar.edu.utn.frba.server.gestorUsuarios.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
