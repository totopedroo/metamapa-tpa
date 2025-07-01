package ar.edu.utn.frba.Fuente_Estatica.Domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;
@Data
public class Hecho {
        private Long idHecho;
        private String titulo;
        private String descripcion;
        private String categoria;
        private Optional<ContenidoMultimedia> contenidoMultimedia;
        private Double latitud;
        private Etiqueta etiqueta;
        private Double longitud;
        private LocalDate fechaAcontecimiento;
        private LocalDate fechaCarga;
        private Boolean estaSincronizado;


    public Hecho(String titulo, String descripcion, String categoria, ContenidoMultimedia contenidoMultimedia,
                 Double latitud, Double longitud, LocalDate fechaAcontecimiento, LocalDate fechaCarga, long idHecho) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.categoria = categoria;
            this.contenidoMultimedia = Optional.ofNullable(contenidoMultimedia);
            this.latitud = latitud;
            this.longitud = longitud;
            this.fechaAcontecimiento = fechaAcontecimiento;
            this.fechaCarga = fechaCarga;
            this.idHecho = idHecho;
            this.estaSincronizado = false;
    }



}
