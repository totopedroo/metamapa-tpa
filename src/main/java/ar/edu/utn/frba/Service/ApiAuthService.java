package ar.edu.utn.frba.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiAuthService {

  private static final String LOGIN_URL = "https://api-ddsi.disilab.ar/public/api/login";

  public String obtenerToken() {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    String requestBody = "{\"email\": \"ddsi@gmail.com\", \"password\": \"ddsi2025*\"}";
    HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(LOGIN_URL, request, Map.class);
    System.out.println("BODY COMPLETO: " + response.getBody());

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      Map<String, Object> responseBody = response.getBody();
      Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
      String token = (String) data.get("access_token");
      System.out.println("TOKEN OBTENIDO: " + token);
      return token;
    }

    throw new RuntimeException("No se pudo obtener el token de autenticación.");
  }
}
