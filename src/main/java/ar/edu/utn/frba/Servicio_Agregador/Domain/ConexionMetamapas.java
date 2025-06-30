package ar.edu.utn.frba.Servicio_Agregador.Domain;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConexionMetamapas {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public List<Hecho> obtenerHechosDesde(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            return mapper.readValue(json, new TypeReference<List<Hecho>>() {});

        } catch (Exception e) {
            System.err.println("Error al obtener hechos desde: " + url);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Hecho> obtenerHechosDesdeMultiples(List<String> urls) {
        List<Hecho> hechos = new ArrayList<>();
        for (String url : urls) {
            hechos.addAll(obtenerHechosDesde(url));
        }
        return hechos;
    }

        public static void main(String[] args) {
            ConexionMetamapas cliente = new ConexionMetamapas();
            List<String> urls = Arrays.asList(
                    "https://metamapa1.com/api/hechos",
                    "https://metamapa2.org/api/hechos"
            );

        List<Hecho> hechosExternos = cliente.obtenerHechosDesdeMultiples(urls);
        hechosExternos.forEach(System.out::println);
    }
}
