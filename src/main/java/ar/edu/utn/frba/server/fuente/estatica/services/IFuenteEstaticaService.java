package ar.edu.utn.frba.server.fuente.estatica.services;

import ar.edu.utn.frba.server.fuente.estatica.domain.Hecho;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFuenteEstaticaService {
    public List<Hecho> sincronizar();

    public void importarHechos(String path);

    public List<Hecho> importarDesdeArchivo(MultipartFile archivo);

    public List<Hecho> importarDesdeRuta(String rutaArchivo);
}
