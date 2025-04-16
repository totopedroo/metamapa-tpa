package mocks;

public class Hecho {

    private String titulo;
    private String descripcion;
    private String categoria;

    public Hecho(String titulo, String descripcion, String categoria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTitulo() {
        return titulo;
    }
}
