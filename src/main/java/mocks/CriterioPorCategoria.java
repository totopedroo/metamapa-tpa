package mocks;

public class CriterioPorCategoria implements CriterioDePertenencia {

    private String categoriaEsperada;

    public CriterioPorCategoria(String categoriaEsperada) {
        this.categoriaEsperada = categoriaEsperada;
    }

    @Override
    public boolean cumple(Hecho hecho) {
        return hecho.getCategoria().equalsIgnoreCase(categoriaEsperada);
    }
}
