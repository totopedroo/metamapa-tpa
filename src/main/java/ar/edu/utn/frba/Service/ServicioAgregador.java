package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.Service.Impl.ColeccionService;
import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.ImportadorAPI;
import ar.edu.utn.frba.domain.ImportadorCSV;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ServicioAgregador {

  private final List<Fuente> fuentes;
  private final IHechosRepository hechosRepository;
  @Autowired
  private ColeccionService coleccionService;

  public ServicioAgregador(ImportadorCSV importadorCSV, ImportadorAPI importadorAPI, IHechosRepository hechosRepository) {
      this.hechosRepository = hechosRepository;
      this.fuentes = List.of(
        new Fuente("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv", importadorCSV, TipoFuente.LOCAL),
        new Fuente("https://api-ddsi.disilab.ar/public/api/desastres", importadorAPI, TipoFuente.PROXY)
    );
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
      hechosRepository.save(hecho);
    }
  }

  @Scheduled (fixedRate = 3600000) // cada 1 hora (en milisegundos)
  public void refrescarHechosPeriodicamente() {
    System.out.println("⏱️ Iniciando refresco automático de colecciones...");

    List<Hecho> nuevosHechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      if (fuente.getTipo() != TipoFuente.PROXY) {
        nuevosHechos.addAll(fuente.obtenerHechos());
      }
    }

    coleccionService.actualizarHechos(nuevosHechos);

<<<<<<< HEAD
    System.out.println("Refresco automático finalizado.");
=======
    System.out.println("✅ Refresco automático finalizado.");
>>>>>>> f28f7f618b28bf86b49c15d85529c94e8e7075d7
  }
}
