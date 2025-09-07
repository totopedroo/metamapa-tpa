package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Data
@Entity
@Table
public class Contribuyente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="nombre")
    public String nombre;
    @Column(name="apellido")
    public String apellido;
    @Column(name="fecha_nacimiento")
    private LocalDate fechaNacimiento;

    public Contribuyente(String nombre, String apellido, LocalDate fechaNacimiento) {
        this.nombre = nombre;this.apellido = apellido;this.fechaNacimiento = fechaNacimiento;
    }

    public Contribuyente(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Contribuyente(String nombre) {
        this.nombre = nombre;
    }

    public Contribuyente() {}


    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
