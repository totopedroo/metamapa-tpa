package ar.edu.utn.frba.server.fuente.proxy.services;

import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastresPageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList; import java.util.List;

@Service @RequiredArgsConstructor
public class DesastresClient {
    private final RestTemplate rt;
    private final ApiAuthService auth;
    private final DesastresProps props;

    /** Trae todas las páginas y filtra localmente por criterio (si viene). */
    public List<DesastreDto> buscar(String criterio) {
        String token = auth.obtenerToken();

        List<DesastreDto> acumulado = new ArrayList<>();
        int page = 1;

        while (true) {
            String url = UriComponentsBuilder
                    .fromHttpUrl(props.getBaseUrl() + "/api/desastres")
                    .queryParam("page", page)
                    .queryParam("per_page", props.getPerPage())
                    .toUriString();

            HttpHeaders h = new HttpHeaders();
            h.setBearerAuth(token);
            h.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<DesastresPageDto> resp =
                    rt.exchange(url, HttpMethod.GET, new HttpEntity<>(h), DesastresPageDto.class);

            DesastresPageDto body = resp.getBody();
            if (body == null || body.getData() == null || body.getData().isEmpty()) break;

            acumulado.addAll(body.getData());
            if (body.getNextPageUrl() == null) break;  // no hay más páginas
            page++;
        }

        if (criterio == null || criterio.isBlank()) return acumulado;
        String c = criterio.toLowerCase();
        return acumulado.stream().filter(d ->
                (d.getTitulo()!=null && d.getTitulo().toLowerCase().contains(c)) ||
                        (d.getDescripcion()!=null && d.getDescripcion().toLowerCase().contains(c)) ||
                        (d.getCategoria()!=null && d.getCategoria().toLowerCase().contains(c))
        ).toList();
    }
}
