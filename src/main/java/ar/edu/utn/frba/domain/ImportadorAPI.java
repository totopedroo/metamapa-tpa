package ar.edu.utn.frba.domain;

import static java.util.stream.Collectors.toList;
import ar.edu.utn.frba.Dtos.DesastreDto;
import ar.edu.utn.frba.Service.ApiAuthService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImportadorAPI implements Importador {

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

    ResponseEntity<Map> response = restTemplate.exchange(
        URL_API,
        HttpMethod.GET,
        entity,
        Map.class
    );

    if (response.getStatusCode().is2xxSuccessful()) {
      Map<String, Object> responseBody = response.getBody();

      List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");

      return data.stream()
          .map(this::mapearAHechoDesdeMap)
          .collect(Collectors.toList());
    }

    throw new RuntimeException("Error al obtener los desastres desde la API");

  }

  private Hecho mapearAHechoDesdeMap(Map<String, Object> map) {
    return new Hecho(
        (String) map.get("titulo"),
        (String) map.get("descripcion"),
        (String) map.get("categoria"),
        null,
        Double.parseDouble(map.get("latitud").toString()),
        Double.parseDouble(map.get("longitud").toString()),
        parsearFechaSegura(map.get("fecha")),
        LocalDate.now(),
        Long.parseLong(map.get("id").toString())
    );
  }

  private static final DateTimeFormatter FORMATO_FECHA_API = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private LocalDate parsearFechaSegura(Object fechaCruda) {
    if (fechaCruda == null) return null;
    try {
      return LocalDate.parse(fechaCruda.toString(), FORMATO_FECHA_API);
    } catch (Exception e) {
      System.err.println("Fecha inválida: " + fechaCruda);
      return null;
    }
  }


}

