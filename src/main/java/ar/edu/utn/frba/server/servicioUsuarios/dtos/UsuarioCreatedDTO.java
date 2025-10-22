package ar.edu.utn.frba.server.servicioUsuarios.dtos;

import ar.edu.utn.frba.server.servicioUsuarios.domain.Rol;

public class UsuarioCreatedDTO {
    private Long id;
    private String username;
    private Rol rol;

    public UsuarioCreatedDTO(Long id, String username, Rol rol) {
        this.id = id; this.username = username; this.rol = rol;
    }
    // getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public Rol getRol() { return rol; }
}
