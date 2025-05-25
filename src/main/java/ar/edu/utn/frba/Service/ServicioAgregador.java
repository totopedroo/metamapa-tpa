package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.ImportadorAPI;
import ar.edu.utn.frba.domain.ImportadorCSV;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServicioAgregador {

  private final List<Fuente> fuentes;

  public ServicioAgregador(ImportadorCSV importadorCSV, ImportadorAPI importadorAPI) {
    this.fuentes = List.of(
        new Fuente("src/main/resources/prueba.csv", importadorCSV),
        new Fuente("", importadorAPI)
    );
  }

  public List<Hecho> agregarHechosDesdeTodasLasFuentes() {
    List<Hecho> hechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      hechos.addAll(fuente.obtenerHechos());
    }
    return hechos;
  }
}
