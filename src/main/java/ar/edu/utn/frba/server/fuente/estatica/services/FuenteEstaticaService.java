package ar.edu.utn.frba.server.fuente.estatica.services;

import ar.edu.utn.frba.server.fuente.estatica.domain.ImportadorCSV;
import ar.edu.utn.frba.server.fuente.estatica.repositories.IHechosEstaticosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.ContenidoMultimedia;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class FuenteEstaticaService implements IFuenteEstaticaService {

    private final ImportadorCSV importador;
    private final EntityManager em;

    @Autowired
    private final IHechosEstaticosRepository repositorio;

    public FuenteEstaticaService(ImportadorCSV importador, EntityManager em, IHechosEstaticosRepository repositorio) {
        this.importador = importador;
        this.em = em;
        this.repositorio = repositorio;
    }

    @Transactional()
    public List<Hecho> obtenerTodosHechos() {
        List<Hecho> hechos = repositorio.findAllFromHecho();

        return hechos.stream()
                .map(h -> Hecho.builder()
                        .idHecho(h.getIdHecho())
                        .titulo(h.getTitulo())
                        .descripcion(h.getDescripcion())
                        .categoria(h.getCategoria())
                        .provincia(h.getProvincia())
                        .latitud(h.getLatitud())
                        .longitud(h.getLongitud())
                        .fechaAcontecimiento(h.getFechaAcontecimiento())
                        .horaAcontecimiento(h.getHoraAcontecimiento())
                        .fechaCarga(h.getFechaCarga())
                        .consensuado(Boolean.TRUE.equals(h.getConsensuado()))
                        .eliminado(h.isEliminado()).build())
                .toList();
    }

    @Override
    public List<Hecho> sincronizar() {
        return List.of();
    }


    @Transactional
    public List<Hecho> importarHechos(String path) {
        List<Hecho> nuevosHechos = importador.importar(path);
        for (Hecho h : nuevosHechos) {

            h.setIdHecho(null);


            ContenidoMultimedia cm = h.getContenidoMultimedia();
            if (cm != null) {

                try {
                    var idField = cm.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(cm, null);
                } catch (Exception ignore) {

                }
            }
            repositorio.save(h);
        }
        em.flush();

        return nuevosHechos;
    }



  /*  public List<Hecho> sincronizar() {
        importarHechos("src/main/resources/prueba1.csv");
        List<Hecho> hechosASincronizar = repositorio.buscarNoSincronizados();
        hechosASincronizar.forEach(h -> h.setEstaSincronizado(true));
        return hechosASincronizar;
    }*/

    @Override
    @Transactional
    public List<Hecho> importarDesdeArchivo(MultipartFile archivo) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            List<Hecho> hechosImportados = new ArrayList<>();

            String line = reader.readLine(); // Leer encabezado y descartarlo

            while ((line = reader.readLine()) != null) {
                Hecho h = importador.parsearLineaCSV(line);
                if (h == null) continue;

                // --- LIMPIAR IDs como en importarHechos(path) ---
                h.setIdHecho(null);

                if (h.getContenidoMultimedia() != null) {
                    try {
                        var idField = h.getContenidoMultimedia().getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(h.getContenidoMultimedia(), null);
                    } catch (Exception ignored) {}
                }

                repositorio.save(h);
                hechosImportados.add(h);
            }

            em.flush();
            return hechosImportados;

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar archivo CSV: " + e.getMessage(), e);
        }
    }
}
