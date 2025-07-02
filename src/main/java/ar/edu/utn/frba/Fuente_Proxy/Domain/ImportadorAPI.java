package ar.edu.utn.frba.Fuente_Proxy.Domain;

import static java.util.stream.Collectors.toList;

import ar.edu.utn.frba.Fuente_Proxy.Domain.ApiDesastresResponse;
import ar.edu.utn.frba.Fuente_Proxy.Domain.Fuente;
import ar.edu.utn.frba.Fuente_Proxy.Domain.Hecho;
import ar.edu.utn.frba.Fuente_Proxy.Domain.Importador;
import ar.edu.utn.frba.Fuente_Proxy.Dtos.DesastreDto;
import ar.edu.utn.frba.Fuente_Proxy.Service.ApiAuthService;
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

@Service("importadorProxyAPI")
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
