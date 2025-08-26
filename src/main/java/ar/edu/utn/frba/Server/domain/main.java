package ar.edu.utn.frba.Server.domain;

import ar.edu.utn.frba.Server.Fuente_Proxy.Dtos.DesastreDto;
import ar.edu.utn.frba.Server.Fuente_Proxy.Service.ApiAuthService;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.Importador;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.Fuente;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.Hecho;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.ApiDesastresResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class main {

  public static void main(String[] args) {
    /*
     * ImportadorCSV importadorCSV = new ImportadorCSV();
     * importadorCSV.importar(new
     * Fuente("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv",importadorCSV,
     * TipoFuente.LOCAL));
     * FuenteDinamicaImpl fuenteDinamica = new FuenteDinamicaImpl();
     * fuenteDinamica.crearHecho(new Contribuyente("ezequiel"), "Accidente",
     * "Accidente en ruta 2", "Accidente automovilistico", null, 70.00, 70.00,
     * LocalDate.now());
     */}

  @Service
  public static class ImportadorAPI implements Importador {

    @Autowired
    private ApiAuthService apiAuthService;

    private static final String URL_API = "https://api-ddsi.disilab.ar/public/api/desastres";

    @Override
    public List<Hecho> importar(Fuente fuente) {
      String token = apiAuthService.obtenerToken();

      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      headers.setAccept(List.of(MediaType.APPLICATION_JSON));

      HttpEntity<String> entity = new HttpEntity<>(headers);

      ResponseEntity<ApiDesastresResponse> response = restTemplate.exchange(
          URL_API,
          HttpMethod.GET,
          entity,
          ApiDesastresResponse.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        List<DesastreDto> desastres = response.getBody().getData();
        // desastres.forEach(dto -> System.out.println("DTO recibido: " + dto));
        return desastres.stream()
            .map(this::mapearAHecho)
            .collect(Collectors.toList());
      }

      throw new RuntimeException("Error al obtener los desastres desde la API");

    }

    private Hecho mapearAHecho(DesastreDto dto) {
      return new Hecho(
          dto.getTitulo(),
          dto.getDescripcion(),
          dto.getCategoria(),
          null,
          dto.getLatitud(),
          dto.getLongitud(),
          dto.getFecha(),
          LocalDate.now(),
          dto.getId());
    }

  }
}
