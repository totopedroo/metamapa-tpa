package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.ImportadorAPI;
import ar.edu.utn.frba.domain.ImportadorCSV;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioAgregador {

  private final List<Fuente> fuentes;
  private final IHechosRepository hechosRepository;

  @Autowired
  public ServicioAgregador(List<Fuente> fuentes, IHechosRepository hechosRepository) {
    this.fuentes = fuentes;
    this.hechosRepository = hechosRepository;
  }

  public List<Hecho> agregarHechosDesdeTodasLasFuentes() {
    List<Hecho> hechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      hechos.addAll(fuente.obtenerHechos());
    }
    return hechos;
  }

  public void sincronizarConRepositorio() {
    List<Hecho> hechos = agregarHechosDesdeTodasLasFuentes();
    //System.out.println("HECHOS OBTENIDOS DE FUENTES: " + hechos);
    for (Hecho hecho : hechos) {
      hechosRepository.guardarHecho(hecho);
    }
  }
}
