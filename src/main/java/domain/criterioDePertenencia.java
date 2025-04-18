package domain;

import java.time.LocalDate;

public class criterioDePertenencia {
    public String columna; // nombre de la columna en el CSV
    public String tipo;    // "texto" o "fecha"
    public String valor;   // para tipo texto
    public LocalDate desde; // para tipo fecha
    public LocalDate hasta;

    // Criterio por texto
    public criterioDePertenencia(String columna, String valor) {
        this.columna = columna;
        this.tipo = "texto";
        this.valor = valor;
    }

    // Criterio por rango de fecha
    public criterioDePertenencia(String columna, LocalDate desde, LocalDate hasta) {
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
                LocalDate fecha = LocalDate.parse(valorEnFila);
                return (fecha.isEqual(desde) || fecha.isAfter(desde)) &&
                        (fecha.isEqual(hasta) || fecha.isBefore(hasta));
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}

