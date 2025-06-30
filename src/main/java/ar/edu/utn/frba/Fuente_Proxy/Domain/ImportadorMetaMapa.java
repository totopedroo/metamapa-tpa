package ar.edu.utn.frba.Fuente_Proxy.Domain;

import ar.edu.utn.frba.Fuente_Proxy.Domain.Fuente;
import ar.edu.utn.frba.Fuente_Proxy.Domain.Hecho;
import ar.edu.utn.frba.Fuente_Proxy.Domain.Importador;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImportadorMetaMapa implements Importador {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Hecho> importar(Fuente fuente) {
        String baseUrl = fuente.getPath(); // ejemplo: http://otra-instancia.com

        try {
            // 1. Obtener colecciones
            HttpResponse<String> coleccionesResp = client.send(
                    HttpRequest.newBuilder().uri(URI.create(baseUrl + "/colecciones")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());

            List<Map<String, Object>> colecciones = mapper.readValue(
                    coleccionesResp.body(), new TypeReference<>() {});

            List<Hecho> hechosFinales = new ArrayList<>();

            for (Map<String, Object> col : colecciones) {
                String id = (String) col.get("id");
                HttpResponse<String> hechosResp = client.send(
                        HttpRequest.newBuilder()
                                .uri(URI.create(baseUrl + "/colecciones/" + id + "/hechos/navegacion?modo=irrestricta"))
                                .GET().build(),
                        HttpResponse.BodyHandlers.ofString());

                List<Hecho> hechos = mapper.readValue(hechosResp.body(), new TypeReference<>() {});
                hechosFinales.addAll(hechos);
            }

            return hechosFinales;

        } catch (Exception e) {
            System.err.println("❌ Error al importar desde MetaMapa: " + baseUrl);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
