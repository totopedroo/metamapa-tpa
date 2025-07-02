package ar.edu.utn.frba.Fuente_Estatica.Service;

import ar.edu.utn.frba.Fuente_Estatica.Domain.Hecho;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFuenteEstaticaService {
    public List<Hecho> sincronizar();

    public void importarHechos(String path);

    public Hecho convertir(Hecho hecho);

    public List<Hecho> importarDesdeArchivo(MultipartFile archivo);

    public List<Hecho> importarDesdeRuta(String rutaArchivo);
}
