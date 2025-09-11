package ar.edu.utn.frba.server.fuente.estatica.services;

import ar.edu.utn.frba.server.fuente.estatica.domain.Hecho;
import ar.edu.utn.frba.server.fuente.estatica.domain.ImportadorCSV;
import ar.edu.utn.frba.server.fuente.estatica.repositories.IHechosEstaticosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FuenteEstaticaService implements IFuenteEstaticaService {

    private final ImportadorCSV importador;
    private final IHechosEstaticosRepository repositorio;

    public void importarHechos(String path) {
        List<Hecho> nuevosHechos = importador.importar(path);
        nuevosHechos.forEach(repositorio::save);
    }

    public List<Hecho> sincronizar() {
        importarHechos("src/main/resources/prueba1.csv");
        List<Hecho> hechosASincronizar = repositorio.buscarNoSincronizados();
        hechosASincronizar.forEach(h -> h.setEstaSincronizado(true));
        return hechosASincronizar;
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

    @Override
    public List<Hecho> importarDesdeRuta(String rutaArchivo) {
        List<Hecho> nuevosHechos = importador.importar(rutaArchivo);
        nuevosHechos.forEach(repositorio::save);
        return nuevosHechos;
    }
}
