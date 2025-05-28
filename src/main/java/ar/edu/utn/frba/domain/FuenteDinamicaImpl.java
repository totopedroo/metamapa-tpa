package ar.edu.utn.frba.domain;

import ar.edu.utn.frba.Repository.IHechosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class FuenteDinamicaImpl implements FuenteDinamica {

    @Autowired
    public  IHechosRepository hechosRepository;


    public Hecho crearHecho(
            Contribuyente contribuyente,
            String titulo,
            String descripcion,
            String categoria,
            ContenidoMultimedia contenidoMultimedia,
            Double latitud,
            Double longitud,
            LocalDate fechaAcontecimiento
    ) {
        // validamos cosas básicass
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }
        if (latitud == null || longitud == null) {
            throw new IllegalArgumentException("La ubicación es obligatoria");
        }
        if (fechaAcontecimiento == null) {
            throw new IllegalArgumentException("La fecha del acontecimiento es obligatoria");
        }

        // Creamos el hecho
        Hecho hecho = new Hecho(
                titulo,
                descripcion,
                categoria,
                contenidoMultimedia,
                latitud,
                longitud,
                fechaAcontecimiento,
                LocalDate.now(),
                System.currentTimeMillis()// ID temporal medio q en una base de datos real esto sería manejado por la BD
        );

        // Guardamos el hecho
        hechosRepository.save(hecho);

        return hecho;
    }

    @Override
    public boolean editarHecho(
            Contribuyente contribuyente,
            Hecho hecho,
            String nuevoTitulo,
            String nuevaDescripcion,
            String nuevaCategoria,
            ContenidoMultimedia nuevoContenidoMultimedia
    ) {
        // Solo contribuyentes registrados pueden editar con esto
        if (contribuyente == null || contribuyente.nombre == null) {
            return false;
        }

        // Verificar que no haya pasado más de una semana para filtrar todo esto
        LocalDate fechaCreacion = hecho.getFechaCarga();
        LocalDate fechaActual = LocalDate.now();
        long diasTranscurridos = ChronoUnit.DAYS.between(fechaCreacion, fechaActual);
        if (diasTranscurridos > 7) {
            return false;
        }

        // Actualizar los campos si no son nulos
        if (nuevoTitulo != null && !nuevoTitulo.trim().isEmpty()) {
            hecho.setTitulo(nuevoTitulo);
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.trim().isEmpty()) {
            hecho.setDescripcion(nuevaDescripcion);
        }
        if (nuevaCategoria != null && !nuevaCategoria.trim().isEmpty()) {
            hecho.setCategoria(nuevaCategoria);
        }
        if (nuevoContenidoMultimedia != null) {
            hecho.setContenidoMultimedia(nuevoContenidoMultimedia);
        }

        return true;
    }
} 