package ar.edu.utn.frba.server.servicioUsuarios.services;

import ar.edu.utn.frba.client.dtos.AuthResponseDTO;
import ar.edu.utn.frba.client.dtos.RolesPermisosDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GestionUsuariosApiHttpService implements GestionUsuariosApiService {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${auth.api.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public AuthResponseDTO login(String username, String password) {
        return webClient.post()
                .uri(baseUrl + "/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("username", username, "password", password))
                .retrieve()
                .bodyToMono(AuthResponseDTO.class)
                .block();
    }

    @Override
    public RolesPermisosDTO getRolesPermisos(String accessToken) {
        return webClient.get()
                .uri(baseUrl + "/auth/roles-permisos")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(RolesPermisosDTO.class)
                .block();
    }
}
