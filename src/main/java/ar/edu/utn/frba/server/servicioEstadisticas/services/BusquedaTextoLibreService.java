package ar.edu.utn.frba.server.servicioEstadisticas.services;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusquedaTextoLibreService {

    @Autowired
    private IHechosRepository hechoRepository;

    /**
     * Realiza búsqueda por texto libre en títulos, descripciones y categorías
     */
    public List<Hecho> buscarPorTextoLibre(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return List.of();
        }

        String textoNormalizado = textoBusqueda.trim().toLowerCase();

        return hechoRepository.findByEliminadoFalse().stream()
                .filter(hecho -> contieneTexto(hecho, textoNormalizado))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un hecho contiene el texto de búsqueda
     */
    private boolean contieneTexto(Hecho hecho, String textoBusqueda) {
        // Buscar en título
        if (hecho.getTitulo() != null &&
                hecho.getTitulo().toLowerCase().contains(textoBusqueda)) {
            return true;
        }

        // Buscar en descripción
        if (hecho.getDescripcion() != null &&
                hecho.getDescripcion().toLowerCase().contains(textoBusqueda)) {
            return true;
        }

        // Buscar en categoría
        if (hecho.getCategoria() != null &&
                hecho.getCategoria().toLowerCase().contains(textoBusqueda)) {
            return true;
        }

        // Buscar en provincia
        if (hecho.getProvincia() != null &&
                hecho.getProvincia().toLowerCase().contains(textoBusqueda)) {
            return true;
        }

        return false;
    }

    /**
     * Búsqueda avanzada con múltiples criterios
     */
    public List<Hecho> busquedaAvanzada(String textoBusqueda, String categoria, String provincia) {
        return hechoRepository.buscarHechos(textoBusqueda, categoria, provincia);
    }

    /**
     * Búsqueda por palabras clave (divide el texto en palabras)
     */
    public List<Hecho> buscarPorPalabrasClave(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return List.of();
        }

        String[] palabras = textoBusqueda.trim().toLowerCase().split("\\s+");

        return hechoRepository.findByEliminadoFalse().stream()
                .filter(hecho -> contieneTodasLasPalabras(hecho, palabras))
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un hecho contiene todas las palabras de búsqueda
     */
    private boolean contieneTodasLasPalabras(Hecho hecho, String[] palabras) {
        String textoCompleto = construirTextoCompleto(hecho).toLowerCase();

        for (String palabra : palabras) {
            if (!textoCompleto.contains(palabra)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Construye un texto completo del hecho para búsqueda
     */
    private String construirTextoCompleto(Hecho hecho) {
        StringBuilder texto = new StringBuilder();

        if (hecho.getTitulo() != null) {
            texto.append(hecho.getTitulo()).append(" ");
        }

        if (hecho.getDescripcion() != null) {
            texto.append(hecho.getDescripcion()).append(" ");
        }

        if (hecho.getCategoria() != null) {
            texto.append(hecho.getCategoria()).append(" ");
        }

        if (hecho.getProvincia() != null) {
            texto.append(hecho.getProvincia()).append(" ");
        }

        return texto.toString();
    }
}