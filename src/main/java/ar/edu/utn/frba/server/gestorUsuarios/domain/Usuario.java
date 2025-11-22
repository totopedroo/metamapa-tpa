package ar.edu.utn.frba.server.gestorUsuarios.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List; // Importante para que funcione la lista

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_de_usuario", unique = true)
    private String nombreDeUsuario;

    private String nombre;

    // El campo que faltaba para el registro
    private String mail;

    private String apellido;

    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Boolean habilitado;
    private Boolean bloqueado;

    /**
     * Método manual para obtener permisos.
     * Esto soluciona el error "cannot find symbol method getPermisos()"
     */
    public List<String> getPermisos() {
        if (this.rol == null) {
            return Collections.emptyList();
        }

        // Lógica simple para devolver permisos según el Rol
        String roleName = this.rol.name();

        if ("ADMIN".equals(roleName)) {
            return List.of("LEER", "EDITAR", "BORRAR", "ADMINISTRAR");
        } else if ("CONTRIBUYENTE".equals(roleName)) {
            return List.of("LEER", "CREAR_HECHOS", "EDITAR_PROPIOS");
        } else {
            // Permisos por defecto
            return List.of("LEER");
        }
    }
}