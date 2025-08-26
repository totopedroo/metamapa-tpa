package ar.edu.utn.frba.Server.config;

import ar.edu.utn.frba.Server.Enums.TipoFuente;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.Fuente;

import java.util.List;

import ar.edu.utn.frba.Server.domain.main;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FuenteConfig {

  /*
   * @Bean
   * public Fuente fuenteCSV(ImportadorCSV importadorCSV) {
   * return new Fuente("src/main/resources/prueba.csv", importadorCSV,
   * TipoFuente.LOCAL);
   * }
   */
  @Bean
  public Fuente fuenteAPI(main.ImportadorAPI importadorAPI) {
    return new Fuente("", importadorAPI, TipoFuente.PROXY);
  }

  @Bean
  public List<Fuente> fuentes(Fuente fuenteCSV, Fuente fuenteAPI) {
    return List.of(fuenteCSV, fuenteAPI);
  }
}
