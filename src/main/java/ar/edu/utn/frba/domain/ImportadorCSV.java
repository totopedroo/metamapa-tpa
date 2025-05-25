package ar.edu.utn.frba.domain;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class ImportadorCSV implements Importador {

  @Override
  public List<Hecho> importar(Fuente fuente) {
    // lógica para importar desde CSV usando fuente.getPath()
    return List.of(); // mock
  }
}
