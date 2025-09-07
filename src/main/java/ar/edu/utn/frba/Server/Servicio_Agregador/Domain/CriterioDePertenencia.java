package ar.edu.utn.frba.Server.Servicio_Agregador.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="criterio_de_pertenencia")
public class CriterioDePertenencia{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_criterio;
    @Column(name="atributo", columnDefinition = "Varchar(40)")
    public String columna; // nombre de la columna en el CSV
    @Column
    public String tipo;    // "texto" o "fecha"
    @Column
    public String valor;
    @Column(name="fecha_desde")// para tipo texto
    public LocalDate desde; // para tipo fecha
    @Column(name="fecha_hasta")
    public LocalDate hasta;

    // Criterio por texto
    public CriterioDePertenencia(String columna, String valor) {
        this.columna = columna;
        this.tipo = "texto";
        this.valor = valor;
    }

    // Criterio por rango de fecha
    public CriterioDePertenencia(String columna, LocalDate desde, LocalDate hasta) {
        this.columna = columna;
        this.tipo = "fecha";
        this.desde = desde;
        this.hasta = hasta;
    }

    public boolean cumple(String valorEnFila) {
        if (tipo.equals("texto")) {
            return valorEnFila.equalsIgnoreCase(valor);
        } else if (tipo.equals("fecha")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                LocalDate fecha = LocalDate.parse(valorEnFila, formatter);
                return (fecha.isEqual(desde) || fecha.isAfter(desde)) &&
                    (fecha.isEqual(hasta) || fecha.isBefore(hasta));
            } catch (Exception e) {
                System.out.println("No se pudo parsear la fecha: " + valorEnFila);
                return false;
            }
        }
        return false;
    }

    public boolean cumple(Hecho hecho) {
        if (tipo.equals("texto")) {
            if (columna.equalsIgnoreCase("titulo")) {
                return hecho.getTitulo().toLowerCase().contains(valor.toLowerCase());
            } else if (columna.equalsIgnoreCase("categoria")) {
                return hecho.getCategoria().toLowerCase().contains(valor.toLowerCase());
            }
        } else if (tipo.equals("fecha")) {
            LocalDate fecha = hecho.getFechaAcontecimiento();
            return (fecha.isEqual(desde) || fecha.isAfter(desde)) &&
                (fecha.isEqual(hasta) || fecha.isBefore(hasta));
        }
        return false;
    }

    public String getColumna() {
        return this.columna;
    }
}
