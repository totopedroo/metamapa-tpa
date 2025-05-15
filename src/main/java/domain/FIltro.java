package domain;

import java.time.LocalDate;

public class FIltro {
    public String columna; // nombre de la columna en el CSV
    public String tipo;    // "texto" o "fecha"
    public String valor;   // para tipo texto
    public LocalDate desde; // para tipo fecha
    public LocalDate hasta;
}
