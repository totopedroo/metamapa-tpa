package ar.edu.utn.frba.server.contratos.enums;


import lombok.Data;
import lombok.Getter;



public enum CampoHecho {
    TITULO("titulo", "Título"),
    DESCRIPCION("descripcion", "Descripción"),
    CATEGORIA("categoria", "Categoría"),
    CONTENIDO_MULTIMEDIA("contenidoMultimedia", "Contenido Multimedia"),
    LATITUD("latitud", "Latitud"),
    LONGITUD("longitud", "Longitud"),
    FECHA_ACONTECIMIENTO("fechaAcontecimiento", "Fecha del Hecho"),
    FECHA_CARGA("fechaCarga", "Fecha de Carga"),
    PROVINCIA("provincia", "Provincia"),
    HORA_ACONTECIMIENTO("horaAcontecimiento", "Hora del Hecho");

    private final String nombreCampo; // El nombre de la variable en Java
    private final String etiqueta;    // Nombre legible para CSV o UI

    CampoHecho(String nombreCampo, String etiqueta) {
        this.nombreCampo = nombreCampo;
        this.etiqueta = etiqueta;
    }

    // Método útil para buscar un enum por el nombre del campo (ej: "fechaAcontecimiento")
    public static CampoHecho fromNombreCampo(String campo) {
        for (CampoHecho c : values()) {
            if (c.nombreCampo.equalsIgnoreCase(campo)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Campo desconocido: " + campo);
    }
}
