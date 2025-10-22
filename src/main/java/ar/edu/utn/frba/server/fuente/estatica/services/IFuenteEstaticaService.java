package ar.edu.utn.frba.server.fuente.estatica.services;

import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFuenteEstaticaService {
    public List<Hecho> sincronizar();

    public List<Hecho> obtenerTodos();
    public List<Hecho> importarHechos(String path);

    public List<Hecho> importarDesdeArchivo(MultipartFile archivo);


}
