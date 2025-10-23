package ar.edu.utn.frba.server.fuente.estatica.services;

import ar.edu.utn.frba.server.servicioAgregador.domain.ContenidoMultimedia;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.domain.ImportadorCSV;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class FuenteEstaticaService implements IFuenteEstaticaService {

    private final ImportadorCSV importador;
    private final EntityManager em;

    @Autowired
    private final IHechosRepository repositorio;

    public FuenteEstaticaService(ImportadorCSV importador, EntityManager em, IHechosRepository repositorio) {
        this.importador = importador;
        this.em = em;
        this.repositorio = repositorio;
    }

    @Transactional
    public List<Hecho> obtenerTodos() {
        return repositorio.findAll();
    }

    @Override
    public List<Hecho> sincronizar() {
        return List.of();
    }

    @Transactional
    public List<Hecho> importarHechos(String path) {
        List<Hecho> nuevosHechos = importador.importar(path);
        System.out.println("Hechos leídos del CSV: " + nuevosHechos.size());

        // Cargar los títulos/lat/long existentes una sola vez
        Set<String> clavesExistentes = repositorio.findAll().stream()
                .map(h -> (h.getTitulo() + "|" + h.getLatitud() + "|" + h.getLongitud()))
                .collect(Collectors.toSet());

        // Filtrar en memoria (muchísimo más rápido que existsBy)
        List<Hecho> nuevos = nuevosHechos.stream()
                .filter(h -> !clavesExistentes.contains(
                        h.getTitulo() + "|" + h.getLatitud() + "|" + h.getLongitud()))
                .toList();

        System.out.println("Nuevos hechos a insertar: " + nuevos.size());

        // Insertar todos en batch
        List<Hecho> guardados = repositorio.saveAll(nuevos);

        System.out.println("Importación finalizada. Total insertados: " + guardados.size());
        return guardados;
    }

    @Override
    public List<Hecho> importarDesdeArchivo(MultipartFile archivo) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {

            List<Hecho> hechosImportados = new ArrayList<>();
            String line = reader.readLine(); // header

            while ((line = reader.readLine()) != null) {
                Hecho hecho = importador.parsearLineaCSV(line);
                if (hecho != null) {
                    repositorio.save(hecho);
                    hechosImportados.add(hecho);
                }
            }
            return hechosImportados;
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar archivo CSV: " + e.getMessage(), e);
        }
    }

}
