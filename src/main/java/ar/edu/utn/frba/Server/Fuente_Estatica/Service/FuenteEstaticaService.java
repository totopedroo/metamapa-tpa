package ar.edu.utn.frba.Server.Fuente_Estatica.Service;

import ar.edu.utn.frba.Server.Fuente_Estatica.Domain.Hecho;
import ar.edu.utn.frba.Server.Fuente_Estatica.Domain.ImportadorCSV;
import ar.edu.utn.frba.Server.Fuente_Estatica.Repository.HechosEstaticosRepository;
import ar.edu.utn.frba.Server.Fuente_Estatica.Repository.IHechosEstaticosRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FuenteEstaticaService implements IFuenteEstaticaService {
    private ImportadorCSV importador;
    private IHechosEstaticosRepository repositorio;

    public FuenteEstaticaService() {
        this.repositorio = new HechosEstaticosRepository();
        this.importador = new ImportadorCSV();
    }
    public List<Hecho> obtenerHechos(){
        return repositorio.findAll();
    }
    public void importarHechos(String path) {
        List<Hecho> nuevosHechos = new ArrayList<>();
        nuevosHechos = importador.importar(path);
        nuevosHechos.stream().forEach(h -> repositorio.save(h));
    }

    public List<Hecho> sincronizar() {
        importarHechos("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv");
        List<Hecho> hechosASincronizar = new ArrayList<>();
        hechosASincronizar = repositorio.buscarNoSicronizados();
        hechosASincronizar.stream().forEach(h -> h.setEstaSincronizado(true));
        return hechosASincronizar.stream().map(h -> convertir(h)).toList();
    }

    @Override
    public List<Hecho> importarDesdeArchivo(MultipartFile archivo) {
        try {
            List<Hecho> hechosImportados = new ArrayList<>();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8));

            // Saltar la primera línea (encabezados)
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                try {
                    Hecho hecho = importador.parsearLineaCSV(line);
                    if (hecho != null) {
                        repositorio.save(hecho);
                        hechosImportados.add(hecho);
                    }
                } catch (Exception e) {
                    System.err.println("Error al procesar línea: " + line + " - " + e.getMessage());
                }
            }
            reader.close();
            return hechosImportados;
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar archivo CSV: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Hecho> importarDesdeRuta(String rutaArchivo) {
        try {
            List<Hecho> nuevosHechos = importador.importar(rutaArchivo);
            nuevosHechos.forEach(h -> repositorio.save(h));
            return nuevosHechos;
        } catch (Exception e) {
            throw new RuntimeException("Error al importar desde ruta: " + rutaArchivo + " - " + e.getMessage(), e);
        }
    }

    public Hecho convertir(Hecho hecho) {
        Hecho h = new Hecho(
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getContenidoMultimedia().orElse(null),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaAcontecimiento(),
                hecho.getFechaAcontecimiento(),
                hecho.getIdHecho());
        return h;
    }
}
