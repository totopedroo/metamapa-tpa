package ar.edu.utn.frba.domain;

import lombok.Data; // ¡Importa esta anotación!
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data // ¡Añade esta anotación!
public class Contribuyente {
    public String nombre;
    public String apellido;
    public Integer edad;

   // Puedes mantener tus constructores existentes; @Data generará el constructor sin argumentos necesario
    public Contribuyente(String nombre, String apellido, Integer edad) {
        this.nombre = nombre;this.apellido = apellido;this.edad = edad;
    }

    public Contribuyente(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Contribuyente(String nombre) {
        this.nombre = nombre;
    }

    public Contribuyente() {}
}
