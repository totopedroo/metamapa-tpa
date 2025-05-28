package ar.edu.utn.frba.domain;

import java.time.LocalDate;

public interface FuenteDinamica {
    /**
     * Crea un nuevo hecho en la fuente dinámica
     *
     * @param contribuyente       El contribuyente que crea el hecho (puede ser anónimo)
     * @param titulo              Título
     * @param descripcion         Descripción
     * @param categoria           Categoría
     * @param contenidoMultimedia Contenido multimedia esto lo dejo opcional
     * @param latitud             Latitud
     * @param longitud            Longitud
     * @param fechaAcontecimiento Fecha
     * @return El hecho creado
     */


    public Hecho crearHecho(
            Contribuyente contribuyente,
            String titulo,
            String descripcion,
            String categoria,
            ContenidoMultimedia contenidoMultimedia,
            Double latitud,
            Double longitud,
            LocalDate fechaAcontecimiento
    );


    boolean editarHecho(
        Contribuyente contribuyente,
        Hecho hecho,
        String nuevoTitulo,
        String nuevaDescripcion,
        String nuevaCategoria,
        ContenidoMultimedia nuevoContenidoMultimedia
    );
} 