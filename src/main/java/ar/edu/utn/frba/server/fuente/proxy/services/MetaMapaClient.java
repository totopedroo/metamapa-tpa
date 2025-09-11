package ar.edu.utn.frba.server.fuente.proxy.services;

import ar.edu.utn.frba.server.fuente.proxy.dtos.MetaMapaHechoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MetaMapaClient {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5)).build();
    private static final ObjectMapper mapper = new ObjectMapper();

    public List<MetaMapaHechoDto> buscarTodos(String baseUrl) {
        try {
            // 1) /colecciones
            var colRes = client.send(
                    HttpRequest.newBuilder(URI.create(baseUrl + "/colecciones")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            List<Map<String,Object>> cols =
                    mapper.readValue(colRes.body(), new TypeReference<>() {});

            List<MetaMapaHechoDto> out = new ArrayList<>();
            for (var col : cols) {
                String id = String.valueOf(col.get("id"));
                var hechosRes = client.send(
                        HttpRequest.newBuilder(
                                        URI.create(baseUrl + "/colecciones/" + id + "/hechos/navegacion?modo=irrestricta"))
                                .GET().build(),
                        HttpResponse.BodyHandlers.ofString());

                out.addAll(mapper.readValue(
                        hechosRes.body(), new TypeReference<List<MetaMapaHechoDto>>() {}));
            }
            return out;
        } catch (Exception e) {
            //log.warn("MetaMapa client error", e);
            return List.of();
        }
    }
}

