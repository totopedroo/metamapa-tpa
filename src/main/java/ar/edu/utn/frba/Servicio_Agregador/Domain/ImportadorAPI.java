package ar.edu.utn.frba.Servicio_Agregador.Domain;

import ar.edu.utn.frba.Servicio_Agregador.Domain.ApiDesastresResponse;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Importador;
import ar.edu.utn.frba.Fuente_Proxy.Dtos.DesastreDto;
import ar.edu.utn.frba.Servicio_Agregador.Service.ApiAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service("importadorAgregadorAPI")
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
                dto.getId()

        );
    }

}
