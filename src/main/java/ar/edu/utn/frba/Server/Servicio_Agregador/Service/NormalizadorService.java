package ar.edu.utn.frba.Server.Servicio_Agregador.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Service
public class NormalizadorService {

    // Mapeo de categorías para normalización
    private final Map<String, String> mapeoCategorias = new HashMap<>();

    // Mapeo de provincias para normalización
    private final Map<String, String> mapeoProvincias = new HashMap<>();

    public NormalizadorService() {
        inicializarMapeoCategorias();
        inicializarMapeoProvincias();
    }

    private void inicializarMapeoCategorias() {
        // Mapeo de variaciones de categorías a valores normalizados
        mapeoCategorias.put("incendio forestal", "Incendio Forestal");
        mapeoCategorias.put("fuego forestal", "Incendio Forestal");
        mapeoCategorias.put("incendio", "Incendio Forestal");
        mapeoCategorias.put("fuego", "Incendio Forestal");

        mapeoCategorias.put("inundacion", "Inundación");
        mapeoCategorias.put("inundaciones", "Inundación");
        mapeoCategorias.put("crecida", "Inundación");
        mapeoCategorias.put("desborde", "Inundación");

        mapeoCategorias.put("terremoto", "Terremoto");
        mapeoCategorias.put("sismo", "Terremoto");
        mapeoCategorias.put("temblor", "Terremoto");

        mapeoCategorias.put("accidente", "Accidente");
        mapeoCategorias.put("accidente automovilistico", "Accidente");
        mapeoCategorias.put("accidente de transito", "Accidente");
        mapeoCategorias.put("choque", "Accidente");

        mapeoCategorias.put("tornado", "Tornado");
        mapeoCategorias.put("huracan", "Huracán");
        mapeoCategorias.put("ciclón", "Huracán");

        mapeoCategorias.put("deslizamiento", "Deslizamiento");
        mapeoCategorias.put("deslave", "Deslizamiento");
        mapeoCategorias.put("derrumbamiento", "Deslizamiento");
    }

    private void inicializarMapeoProvincias() {
        // Mapeo de variaciones de provincias a valores normalizados
        mapeoProvincias.put("caba", "Ciudad Autónoma de Buenos Aires");
        mapeoProvincias.put("buenos aires", "Buenos Aires");
        mapeoProvincias.put("bs as", "Buenos Aires");
        mapeoProvincias.put("bs.as.", "Buenos Aires");
        mapeoProvincias.put("córdoba", "Córdoba");
        mapeoProvincias.put("cordoba", "Córdoba");
        mapeoProvincias.put("santa fe", "Santa Fe");
        mapeoProvincias.put("santafe", "Santa Fe");
        mapeoProvincias.put("mendoza", "Mendoza");
        mapeoProvincias.put("tucumán", "Tucumán");
        mapeoProvincias.put("tucuman", "Tucumán");
        mapeoProvincias.put("salta", "Salta");
        mapeoProvincias.put("entre ríos", "Entre Ríos");
        mapeoProvincias.put("entre rios", "Entre Ríos");
        mapeoProvincias.put("corrientes", "Corrientes");
        mapeoProvincias.put("santiago del estero", "Santiago del Estero");
        mapeoProvincias.put("san juan", "San Juan");
        mapeoProvincias.put("jujuy", "Jujuy");
        mapeoProvincias.put("río negro", "Río Negro");
        mapeoProvincias.put("rio negro", "Río Negro");
        mapeoProvincias.put("formosa", "Formosa");
        mapeoProvincias.put("chaco", "Chaco");
        mapeoProvincias.put("san luis", "San Luis");
        mapeoProvincias.put("catamarca", "Catamarca");
        mapeoProvincias.put("la rioja", "La Rioja");
        mapeoProvincias.put("la pampa", "La Pampa");
        mapeoProvincias.put("neuquén", "Neuquén");
        mapeoProvincias.put("neuquen", "Neuquén");
        mapeoProvincias.put("chubut", "Chubut");
        mapeoProvincias.put("santa cruz", "Santa Cruz");
        mapeoProvincias.put("tierra del fuego", "Tierra del Fuego");
        mapeoProvincias.put("misiones", "Misiones");
    }

    /**
     * Normaliza una categoría de hecho
     */
    public String normalizarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return "Sin categoría";
        }

        String categoriaNormalizada = categoria.trim().toLowerCase();
        return mapeoCategorias.getOrDefault(categoriaNormalizada, categoria);
    }

    /**
     * Normaliza una provincia
     */
    public String normalizarProvincia(String provincia) {
        if (provincia == null || provincia.trim().isEmpty()) {
            return "Sin provincia";
        }

        String provinciaNormalizada = provincia.trim().toLowerCase();
        return mapeoProvincias.getOrDefault(provinciaNormalizada, provincia);
    }

    /**
     * Normaliza una fecha en formato string
     */
    public LocalDate normalizarFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return null;
        }

        String fecha = fechaStr.trim();

        // Lista de formatos de fecha comunes
        DateTimeFormatter[] formatos = {
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("d/M/yy"),
                DateTimeFormatter.ofPattern("dd/MM/yy")
        };

        for (DateTimeFormatter formato : formatos) {
            try {
                return LocalDate.parse(fecha, formato);
            } catch (DateTimeParseException e) {
                // Continuar con el siguiente formato
            }
        }

        // Si no se puede parsear, retornar null
        return null;
    }

    /**
     * Normaliza un título de hecho
     */
    public String normalizarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return "Sin título";
        }

        // Capitalizar primera letra de cada palabra
        String[] palabras = titulo.trim().split("\\s+");
        StringBuilder tituloNormalizado = new StringBuilder();

        for (int i = 0; i < palabras.length; i++) {
            if (i > 0)
                tituloNormalizado.append(" ");
            if (!palabras[i].isEmpty()) {
                tituloNormalizado.append(palabras[i].substring(0, 1).toUpperCase())
                        .append(palabras[i].substring(1).toLowerCase());
            }
        }

        return tituloNormalizado.toString();
    }

    /**
     * Normaliza una descripción
     */
    public String normalizarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return "Sin descripción";
        }

        // Limpiar espacios múltiples y normalizar
        return descripcion.trim().replaceAll("\\s+", " ");
    }

    /**
     * Valida si una coordenada es válida
     */
    public boolean validarCoordenada(Double coordenada) {
        return coordenada != null && !Double.isNaN(coordenada) && !Double.isInfinite(coordenada);
    }

    /**
     * Valida latitud (debe estar entre -90 y 90)
     */
    public boolean validarLatitud(Double latitud) {
        if (!validarCoordenada(latitud)) {
            return false;
        }
        return latitud >= -90.0 && latitud <= 90.0;
    }

    /**
     * Valida longitud (debe estar entre -180 y 180)
     */
    public boolean validarLongitud(Double longitud) {
        if (!validarCoordenada(longitud)) {
            return false;
        }
        return longitud >= -180.0 && longitud <= 180.0;
    }

    /**
     * Normaliza formato de coordenada (comas a puntos)
     */
    public Double normalizarFormatoCoordenada(String coordenadaStr) {
        if (coordenadaStr == null || coordenadaStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Reemplazar comas por puntos para formato decimal
            String coordenadaNormalizada = coordenadaStr.trim().replace(",", ".");
            return Double.parseDouble(coordenadaNormalizada);
        } catch (NumberFormatException e) {
            return null; // Formato inválido
        }
    }

    /**
     * Normaliza latitud (rechaza null realmente)
     */
    public Double normalizarLatitud(Double latitud) {
        // Rechazar null realmente
        if (latitud == null) {
            return null;
        }

        // Validar rango
        if (!validarLatitud(latitud)) {
            return null;
        }

        return latitud;
    }

    /**
     * Normaliza longitud (rechaza null realmente)
     */
    public Double normalizarLongitud(Double longitud) {
        // Rechazar null realmente
        if (longitud == null) {
            return null;
        }

        // Validar rango
        if (!validarLongitud(longitud)) {
            return null;
        }

        return longitud;
    }
}
