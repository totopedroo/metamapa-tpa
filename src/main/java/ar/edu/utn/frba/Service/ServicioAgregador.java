package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.Service.Impl.ColeccionService;
import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.ImportadorAPI;
import ar.edu.utn.frba.domain.ImportadorCSV;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ServicioAgregador {

  private final List<Fuente> fuentes;
  @Autowired
  private ColeccionService coleccionService;

  public ServicioAgregador(ImportadorCSV importadorCSV, ImportadorAPI importadorAPI) {
    this.fuentes = List.of(
        new Fuente("src/main/resources/prueba.csv", importadorCSV, TipoFuente.LOCAL),
        new Fuente("", importadorAPI, TipoFuente.PROXY)
    );
  }

  public List<Hecho> agregarHechosDesdeTodasLasFuentes() {
    List<Hecho> hechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      hechos.addAll(fuente.obtenerHechos());
    }
    return hechos;
  }

  @Scheduled(fixedRate = 3600000) // cada 1 hora (en milisegundos)
  public void refrescarHechosPeriodicamente() {
    System.out.println("⏱️ Iniciando refresco automático de colecciones...");

    List<Hecho> nuevosHechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      if (fuente.getTipo() != TipoFuente.PROXY) {
        nuevosHechos.addAll(fuente.obtenerHechos());
      }
    }

    coleccionService.actualizarHechos(nuevosHechos);

    System.out.println("✅ Refresco automático finalizado.");
  }
}
