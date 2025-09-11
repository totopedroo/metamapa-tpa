package ar.edu.utn.frba.server.fuente.proxy.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service @RequiredArgsConstructor
public class ApiAuthService {
    private final RestTemplate rt;
    private final DesastresProps props;

    public String obtenerToken() {
        String url = props.getBaseUrl() + "/api/login";
        HttpHeaders h = new HttpHeaders(); h.setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", props.getEmail(), props.getPassword());

        ResponseEntity<Map> resp = rt.postForEntity(url, new HttpEntity<>(body, h), Map.class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("No se pudo obtener token de API Desastres");
        }
        Map data = (Map) resp.getBody().get("data");
        return (String) data.get("access_token");
    }
}
