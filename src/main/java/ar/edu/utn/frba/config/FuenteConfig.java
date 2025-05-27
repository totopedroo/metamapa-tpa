package ar.edu.utn.frba.config;

import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.domain.Fuente;
import ar.edu.utn.frba.domain.ImportadorAPI;
import ar.edu.utn.frba.domain.ImportadorCSV;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FuenteConfig {

  @Bean
  public Fuente fuenteCSV(ImportadorCSV importadorCSV) {
    return new Fuente("src/main/resources/prueba.csv", importadorCSV, TipoFuente.LOCAL);
  }

  @Bean
  public Fuente fuenteAPI(ImportadorAPI importadorAPI) {
    return new Fuente("", importadorAPI, TipoFuente.PROXY);
  }

  @Bean
  public List<Fuente> fuentes(Fuente fuenteCSV, Fuente fuenteAPI) {
    return List.of(fuenteCSV, fuenteAPI);
  }
}
