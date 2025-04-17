package domain;

public class Hecho {
  package domain; // hechos branch


import lombok.Getter;
import java.time.LocalDate;
import java.util.Optional;


@Getter
public class Hecho {
    public String titulo;
    public String descripcion;
    public String categoria;
    public Optional<ContenidoMultimedia> contenidoMultimedia;
    public Double latitud;
    public Double longitud;
    public LocalDate fechaAcontecimiento;
    public LocalDate fechaCarga;
    public Origen origen;


    public Hecho(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia,
                 Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga,
                 Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.contenidoMultimedia = Optional.ofNullable(contenidoMultimedia);
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.origen = origen;
    }
}
}
