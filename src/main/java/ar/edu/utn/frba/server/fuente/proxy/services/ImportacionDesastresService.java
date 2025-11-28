package ar.edu.utn.frba.server.fuente.proxy.services;
import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import ar.edu.utn.frba.server.fuente.dinamica.repositories.IHechosDinamicosRepository;
import ar.edu.utn.frba.server.fuente.proxy.mappers.DesastresApiMapper;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportacionDesastresService {

    private static final String API_URL = "https://api-ddsi.disilab.ar/public/api/desastres";

    private final RestTemplate restTemplate;
    private final DesastresApiMapper mapper;
    private final IHechosDinamicosRepository hechosRepo;

    /** Solo consulta la API (no guarda). */
    public List<Hecho> preview() {
        ResponseEntity<DesastreDto[]> resp =
                restTemplate.getForEntity(API_URL, DesastreDto[].class);
        DesastreDto[] body = resp.getBody();
        if (body == null) return List.of();
        return Arrays.stream(body).map(mapper::toHecho).toList();
    }

    /** Trae de la API y guarda en BD evitando duplicados por (titulo, fecha). */
    @Transactional
    public List<Hecho> sincronizar() {
        ResponseEntity<DesastreDto[]> resp =
                restTemplate.getForEntity(API_URL, DesastreDto[].class);
        DesastreDto[] body = resp.getBody();
        if (body == null) return List.of();

        List<Hecho> nuevos = new ArrayList<>();
        for (DesastreDto dto : body) {
            Hecho h = mapper.toHecho(dto);
            String t = h.getTitulo();
            LocalDate f = h.getFechaAcontecimiento();

            boolean existe = t != null && !t.isBlank() && f != null &&
                    hechosRepo.findByTituloAndFechaAcontecimiento(t, f).isPresent();

            if (!existe) {
                nuevos.add(h);
            }
        }
        return hechosRepo.saveAll(nuevos);
    }
}