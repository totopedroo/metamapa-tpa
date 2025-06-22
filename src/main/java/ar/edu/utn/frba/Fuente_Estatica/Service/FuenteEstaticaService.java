package ar.edu.utn.frba.Fuente_Estatica.Service;

import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.Fuente_Estatica.Domain.HechoEstatico;
import ar.edu.utn.frba.Fuente_Estatica.Domain.ImportadorCSV;
import ar.edu.utn.frba.Fuente_Estatica.Repository.HechosEstaticosRepository;
import ar.edu.utn.frba.Fuente_Estatica.Repository.IHechosEstaticosRepository;
import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.Importador;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class FuenteEstaticaService implements IFuenteEstaticaService{
    private ImportadorCSV importador;
    private IHechosEstaticosRepository repositorio;

    public FuenteEstaticaService() {
        this.repositorio = new HechosEstaticosRepository();
        this.importador = new ImportadorCSV();
    }

    public void importarHechos(String path){
        List<HechoEstatico> nuevosHechos = new ArrayList<>();
        nuevosHechos = importador.importar(path);
        nuevosHechos.stream().forEach(h->repositorio.save(h));
    }

    public List<Hecho> sincronizar(){
        importarHechos("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv");
        List<HechoEstatico> hechosASincronizar = new ArrayList<>();
        hechosASincronizar = repositorio.buscarNoSicronizados();
        hechosASincronizar.stream().forEach(h-> h.setEstaSincronizado(true));
        return hechosASincronizar.stream().map(h->convertir(h)).toList();

    }

    public Hecho convertir(HechoEstatico hecho) {
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
