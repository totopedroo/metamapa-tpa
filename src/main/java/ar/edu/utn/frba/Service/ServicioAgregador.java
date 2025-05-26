package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.ImportadorAPI;
import ar.edu.utn.frba.domain.ImportadorCSV;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableScheduling
@Service
public class ServicioAgregador {

  private final List<Fuente> fuentes;
  private final ServicioColecciones servicioColecciones;

  public ServicioAgregador(ImportadorCSV importadorCSV, ImportadorAPI importadorAPI, ServicioColecciones servicioColecciones) {
    this.fuentes = List.of(
        new Fuente("src/main/resources/prueba.csv", importadorCSV, TipoFuente.LOCAL),
        new Fuente("", importadorAPI, TipoFuente.PROXY)
    );
    this.servicioColecciones = servicioColecciones;
  }

  public List<Hecho> agregarHechosDesdeTodasLasFuentes() {
    List<Hecho> hechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      hechos.addAll(fuente.obtenerHechos());
    }
    return hechos;
  }

  // Refresca solo desde fuentes no proxy (cada hora)
  @Scheduled(fixedRate = 3600000) // 1 hora = 3600000 ms
  public void refrescarColeccionesCadaHora() {
    System.out.println("Ejecutando refresco de colecciones...");

    List<Hecho> nuevosHechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      if (!fuente.esFuenteProxy()) {
        nuevosHechos.addAll(fuente.obtenerHechos());
      }
    }

    servicioColecciones.actualizarHechos(nuevosHechos);

    System.out.println("Refresco de colecciones completado.");
  }
}
