package ar.edu.utn.frba.server.servicioAgregador.domain;


import ar.edu.utn.frba.server.servicioAgregador.domain.ApiDesastresResponse;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import ar.edu.utn.frba.server.fuente.proxy.services.ApiAuthService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImportadorAPI {

    private static final String API_URL = "https://api-ddsi.disilab.ar/public/api/desastres";

    static class DesastreDto {
        public Long id;
        public String titulo;
        public String descripcion;
        public String categoria;
        public Double latitud;
        public Double longitud;
        @JsonProperty("fecha_hecho")
        public OffsetDateTime fechaHecho;
    }

    public List<Hecho> importarDesdeApi() {
        try {
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> resp = rt.getForEntity(API_URL, String.class);

            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            JsonNode root = mapper.readTree(resp.getBody());
            JsonNode array = root.isArray() ? root : (root.has("data") ? root.get("data") : null);

            List<Hecho> hechos = new ArrayList<>();
            if (array == null || !array.isArray()) return hechos;

            for (JsonNode n : array) {
                DesastreDto dto = mapper.treeToValue(n, DesastreDto.class);

                Hecho h = new Hecho();
                h.setTitulo(dto.titulo);
                h.setDescripcion(dto.descripcion);
                h.setCategoria(dto.categoria);
                h.setLatitud(dto.latitud);
                h.setLongitud(dto.longitud);
                if (dto.fechaHecho != null) {
                    h.setFechaAcontecimiento(dto.fechaHecho.toLocalDate());
                    h.setHoraAcontecimiento(dto.fechaHecho.toLocalTime());
                }
                h.setFechaCarga(LocalDate.now());

                hechos.add(h);
            }
            return hechos;

        } catch (Exception e) {
            throw new RuntimeException("No se pudo consumir la API de desastres: " + e.getMessage(), e);
        }
    }
}