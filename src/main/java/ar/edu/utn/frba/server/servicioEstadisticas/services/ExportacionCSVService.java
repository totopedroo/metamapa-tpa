package ar.edu.utn.frba.server.servicioEstadisticas.services;

import ar.edu.utn.frba.server.servicioAgregador.domain.ImportadorCSV;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportacionCSVService {

    @Autowired
    private IHechosRepository hechoRepository;
    private ImportadorCSV importador;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");


    public ExportacionCSVService(@Qualifier("importadorCSVAgregador") ImportadorCSV importador) {
        this.importador = importador;
    }

    public List<Hecho> importarDesdeRuta(String rutaArchivo) {
        List<Hecho> nuevosHechos = importador.importar(rutaArchivo);
        nuevosHechos.forEach(hechoRepository::save);
        return nuevosHechos;
    }
    /**
     * Exporta todos los hechos a CSV
     */
    public byte[] exportarHechos() throws IOException {
        List<Hecho> hechos = hechoRepository.findByEliminadoFalse();
        return generarCSVHechos(hechos);
    }

    /**
     * Exporta hechos por provincia a CSV
     */
    public byte[] exportarHechosPorProvincia(String provincia) throws IOException {
        List<Hecho> hechos = hechoRepository.findByProvinciaAndEliminadoFalse(provincia);
        return generarCSVHechos(hechos);
    }

    /**
     * Exporta hechos por categoría a CSV
     */
    public byte[] exportarHechosPorCategoria(String categoria) throws IOException {
        List<Hecho> hechos = hechoRepository.findByCategoriaAndEliminadoFalse(categoria);
        return generarCSVHechos(hechos);
    }

    /**
     * Exporta estadísticas generales a CSV
     */
    public byte[] exportarEstadisticas() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Encabezados
            csvWriter.writeNext(new String[] {
                    "Tipo Estadística", "Valor", "Cantidad"
            });

            // Estadísticas de provincias
            List<Object[]> hechosPorProvincia = hechoRepository.contarHechosPorProvincia();
            for (Object[] resultado : hechosPorProvincia) {
                csvWriter.writeNext(new String[] {
                        "Provincia",
                        (String) resultado[0],
                        resultado[1].toString()
                });
            }

            // Estadísticas de categorías
            List<Object[]> hechosPorCategoria = hechoRepository.contarHechosPorCategoria();
            for (Object[] resultado : hechosPorCategoria) {
                csvWriter.writeNext(new String[] {
                        "Categoría",
                        (String) resultado[0],
                        resultado[1].toString()
                });
            }
        }

        return outputStream.toByteArray();
    }

    /**
     * Genera el CSV de hechos
     */
    private byte[] generarCSVHechos(List<Hecho> hechos) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            // Encabezados
            csvWriter.writeNext(new String[] {
                    "ID", "Título", "Descripción", "Categoría", "Provincia",
                    "Latitud", "Longitud", "Fecha Acontecimiento", "Hora Acontecimiento",
                    "Fecha Carga", "Eliminado", "Consensuado"
            });

            // Datos
            for (Hecho hecho : hechos) {
                csvWriter.writeNext(new String[] {
                        hecho.getIdHecho() != null ? hecho.getIdHecho().toString() : "",
                        hecho.getTitulo() != null ? hecho.getTitulo() : "",
                        hecho.getDescripcion() != null ? hecho.getDescripcion() : "",
                        hecho.getCategoria() != null ? hecho.getCategoria() : "",
                        hecho.getProvincia() != null ? hecho.getProvincia() : "",
                        hecho.getLatitud() != null ? hecho.getLatitud().toString() : "",
                        hecho.getLongitud() != null ? hecho.getLongitud().toString() : "",
                        hecho.getFechaAcontecimiento() != null ? hecho.getFechaAcontecimiento().format(DATE_FORMATTER)
                                : "",
                        hecho.getHoraAcontecimiento() != null ? hecho.getHoraAcontecimiento().format(TIME_FORMATTER)
                                : "",
                        hecho.getFechaCarga() != null ? hecho.getFechaCarga().format(DATE_FORMATTER) : "",
                        String.valueOf(hecho.isEliminado()),
                        hecho.getConsensuado() != null ? hecho.getConsensuado().toString() : ""
                });
            }
        }

        return outputStream.toByteArray();
    }

    /**
     * Exporta estadísticas por colección a CSV
     */
    public byte[] exportarEstadisticasPorColeccion(Long coleccionId) throws IOException {
        List<Hecho> hechos = hechoRepository.findByColeccionId(coleccionId);
        return generarCSVHechos(hechos);
    }
}