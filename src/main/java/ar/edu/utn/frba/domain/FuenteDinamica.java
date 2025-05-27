package ar.edu.utn.frba.domain;

import java.time.LocalDate;

public interface FuenteDinamica {
    /**
     * Crea un nuevo hecho en la fuente dinámica
     * @param contribuyente El contribuyente que crea el hecho (puede ser anónimo)
     * @param titulo Título 
     * @param descripcion Descripción
     * @param categoria Categoría 
     * @param contenidoMultimedia Contenido multimedia esto lo dejo opcional
     * @param latitud Latitud 
     * @param longitud Longitud 
     * @param fechaAcontecimiento Fecha 
     * @return El hecho creado
     */
    Hecho crearHecho(
        Contribuyente contribuyente,
        String titulo,
        String descripcion,
        String categoria,
        ContenidoMultimedia contenidoMultimedia,
        Double latitud,
        Double longitud,
        LocalDate fechaAcontecimiento
    );

    /**
     * Edita un hecho existente si el contribuyente está registrado y no pasó más de una semana
     * @param contribuyente El contribuyente que quiere editar el hecho
     * @param hecho El hecho a editar
     * @param nuevoTitulo Nuevo título
     * @param nuevaDescripcion Nueva descripción
     * @param nuevaCategoria Nueva categoría
     * @param nuevoContenidoMultimedia Nuevo contenido multimedia
     * @return true si se pudo editar, false si no
     */
    boolean editarHecho(
        Contribuyente contribuyente,
        Hecho hecho,
        String nuevoTitulo,
        String nuevaDescripcion,
        String nuevaCategoria,
        ContenidoMultimedia nuevoContenidoMultimedia
    );
} 