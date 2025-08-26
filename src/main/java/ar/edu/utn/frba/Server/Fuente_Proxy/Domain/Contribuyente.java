package ar.edu.utn.frba.Server.Fuente_Proxy.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Data
public class Contribuyente {
    public String nombre;
    public String apellido;

    @JsonProperty("fecha_nacimiento")
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

    @JsonIgnore
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
