package ar.edu.utn.frba.server.gestorUsuarios.dtos;

import ar.edu.utn.frba.server.gestorUsuarios.domain.Rol;

public class UsuarioDtoInput {
    private String username;
    private String password;
    private String rol;               // ADMIN | CONTRIBUYENTE | VISUALIZADOR


    // getters/setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}