package ar.edu.utn.frba.domain;

import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.Repository.Implementacion.HechosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FuenteDinamicaImplTest {

    private IHechosRepository hechosRepository;
    private FuenteDinamicaImpl fuenteDinamica;
    private Contribuyente contribuyenteRegistrado;
    private Contribuyente contribuyenteAnonimo;

    @BeforeEach
    void setUp() {
        hechosRepository = new HechosRepository();
        fuenteDinamica = new FuenteDinamicaImpl();
        fuenteDinamica.hechosRepository = hechosRepository;
        
        // Crear contribuyentes para las pruebas
        contribuyenteRegistrado = new Contribuyente("Juan", "Perez", 25);
        contribuyenteAnonimo = null;
    }

    @Test
    void crearHechoExitoso() {
        // Arrange
        String titulo = "Incendio en el Parque";
        String descripcion = "Se reporta un incendio en el parque central";
        String categoria = "Incendio";
        Double latitud = -34.603722;
        Double longitud = -58.381592;
        LocalDate fechaAcontecimiento = LocalDate.now();

        // Act
        Hecho hecho = fuenteDinamica.crearHecho(
            contribuyenteRegistrado,
            titulo,
            descripcion,
            categoria,
            null, // sin contenido multimedia
            latitud,
            longitud,
            fechaAcontecimiento
        );

        // Assert
        assertNotNull(hecho);
        assertEquals(titulo, hecho.getTitulo());
        assertEquals(descripcion, hecho.getDescripcion());
        assertEquals(categoria, hecho.getCategoria());
        assertTrue(hechosRepository.findAll().contains(hecho));
    }

    @Test
    void crearHechoAnonimo() {
        // Arrange
        String titulo = "Incendio en el Parque";
        String descripcion = "Se reporta un incendio en el parque central";
        String categoria = "Incendio";
        Double latitud = -34.603722;
        Double longitud = -58.381592;
        LocalDate fechaAcontecimiento = LocalDate.now();

        // Act
        Hecho hecho = fuenteDinamica.crearHecho(
            contribuyenteAnonimo,
            titulo,
            descripcion,
            categoria,
            null,
            latitud,
            longitud,
            fechaAcontecimiento
        );

        // Assert
        assertNotNull(hecho);
        assertTrue(hechosRepository.findAll().contains(hecho));
    }

    @Test
    void crearHechoConCamposInvalidos() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            fuenteDinamica.crearHecho(
                contribuyenteRegistrado,
                "", // título vacío
                "descripcion",
                "categoria",
                null,
                -34.603722,
                -58.381592,
                LocalDate.now()
            );
        });
    }

    @Test
    void editarHechoExitoso() {
        // Arrange
        Hecho hechoExistente = new Hecho(
            "Título original",
            "Descripción original",
            "Categoría original",
            null,
            -34.603722,
            -58.381592,
            LocalDate.now(),
            LocalDate.now(),
            1L
        );
        hechosRepository.guardarHecho(hechoExistente);

        // Act
        boolean resultado = fuenteDinamica.editarHecho(
            contribuyenteRegistrado,
            hechoExistente,
            "Nuevo título",
            "Nueva descripción",
            "Nueva categoría",
            null
        );

        // Assert
        assertTrue(resultado);
        assertEquals("Nuevo título", hechoExistente.getTitulo());
        assertEquals("Nueva descripción", hechoExistente.getDescripcion());
        assertEquals("Nueva categoría", hechoExistente.getCategoria());
    }

    @Test
    void editarHechoContribuyenteAnonimo() {
        // Arrange
        Hecho hechoExistente = new Hecho(
            "Título original",
            "Descripción original",
            "Categoría original",
            null,
            -34.603722,
            -58.381592,
            LocalDate.now(),
            LocalDate.now(),
            1L
        );
        hechosRepository.guardarHecho(hechoExistente);

        // Act
        boolean resultado = fuenteDinamica.editarHecho(
            contribuyenteAnonimo,
            hechoExistente,
            "Nuevo título",
            "Nueva descripción",
            "Nueva categoría",
            null
        );

        // Assert
        assertFalse(resultado);
        assertEquals("Título original", hechoExistente.getTitulo());
    }

    @Test
    void editarHechoDespuesDeUnaSemana() {
        // Arrange
        Hecho hechoExistente = new Hecho(
            "Título original",
            "Descripción original",
            "Categoría original",
            null,
            -34.603722,
            -58.381592,
            LocalDate.now().minusDays(10),
            LocalDate.now().minusDays(10),
            1L
        );
        hechosRepository.guardarHecho(hechoExistente);

        // Act
        boolean resultado = fuenteDinamica.editarHecho(
            contribuyenteRegistrado,
            hechoExistente,
            "Nuevo título",
            "Nueva descripción",
            "Nueva categoría",
            null
        );

        // Assert
        assertFalse(resultado);
        assertEquals("Título original", hechoExistente.getTitulo());
    }
} 