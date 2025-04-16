package mocks;

public class Coleccion {

    private String titulo;
    private String descripcion;
    private Fuente fuente;
    private CriterioDePertenencia criterio;

    public Coleccion(String titulo, String descripcion, Fuente fuente, CriterioDePertenencia criterio) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuente = fuente;
        this.criterio = criterio;
    }

    public String getTitulo() {
        return titulo;
    }
}
