package ar.edu.utn.frba.Server.Fuente_Proxy.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service("apiAuthProxyService")
public class ApiAuthService {

    private static final String LOGIN_URL = "https://api-ddsi.disilab.ar/public/api/login";

    public String obtenerToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"email\": \"ddsi@gmail.com\", \"password\": \"ddsi2025*\"}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(LOGIN_URL, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            String token = (String) data.get("access_token");
            // System.out.println("TOKEN OBTENIDO: " + token);
            return token;
        }

        throw new RuntimeException("No se pudo obtener el token de autenticación.");
    }
}
