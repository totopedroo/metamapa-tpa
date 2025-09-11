package ar.edu.utn.frba.server.contratos.enums;

public enum TipoAlgoritmoConsenso {
    MULTIPLES_MENCIONES("multiplesMenciones"),
    MAYORIA_SIMPLE("mayoriaSimple"),
    ABSOLUTA("absoluta"),
    DEFECTO("defecto");

    private final String codigo;

    TipoAlgoritmoConsenso(String codigo) { this.codigo = codigo; }

    public String codigo() { return codigo; }

    public static TipoAlgoritmoConsenso fromCodigo(String codigo) {
        if (codigo == null) throw new IllegalArgumentException("codigo nulo");
        for (var t : values()) {
            if (t.codigo.equalsIgnoreCase(codigo)) return t;
        }
        throw new IllegalArgumentException("Código de algoritmo inválido: " + codigo);
    }
}