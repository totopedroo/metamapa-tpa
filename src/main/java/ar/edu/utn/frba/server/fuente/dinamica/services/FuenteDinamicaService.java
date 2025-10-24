package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.repositories.IHechosDinamicosRepository;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import ar.edu.utn.frba.server.fuente.dinamica.domain.ContenidoMultimedia;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FuenteDinamicaService implements IFuenteDinamicaService {

    private final IHechosDinamicosRepository hechosRepository;
    private final ApiDinamicaMapper apiMapper; // tu mapper

    @Override
    @Transactional
    public HechosOutputDto crearHecho(HechosInputDto inputDto) {
        // Validaciones básicas (mejor si usas Bean Validation con @Valid en el controller)
        if (inputDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body requerido");
        }
        if (inputDto.getTitulo() == null || inputDto.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }

        // Fecha: si viene null o vacía, podés decidir default o rechazar
        LocalDate fechaAcontecimiento = inputDto.getFechaAcontecimiento();
        // si tu DTO trae fecha como String, parseala aquí con try/catch y BAD_REQUEST si falla

        // Contenido multimedia: si viene sin url, lo dejamos en null
        var cmDto = inputDto.getContenidoMultimedia();
        var contenidoMultimedia = (cmDto != null && cmDto.getUrl() != null && !cmDto.getUrl().isBlank())
                ? apiMapper.toContenidoMultimedia(cmDto)
                : null;

        // Construcción del Hecho (sin setear idHecho: que lo genere la DB)
        Hecho hecho = Hecho.builder()
                .titulo(nullToEmpty(inputDto.getTitulo()))
                .descripcion(nullToEmpty(inputDto.getDescripcion()))
                .categoria(nullToEmpty(inputDto.getCategoria()))
                .contenidoMultimedia(contenidoMultimedia)
                .latitud(inputDto.getLatitud())
                .longitud(inputDto.getLongitud())
                .fechaAcontecimiento(fechaAcontecimiento)
                .fechaCarga(LocalDate.now())
                .build();


        Hecho guardado = hechosRepository.save(hecho);
        // flush opcional para surfear violaciones de constraint ya mismo
        // hechosRepository.flush();

        return apiMapper.toOutput(guardado);
    }
    public ApiDinamicaMapper getApiMapper() { return apiMapper; }


    @Override
    @Transactional
    public HechosOutputDto editarHecho(Long id, HechosOutputDto out) {
        Hecho h = hechosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrado: " + id));

        if (out.getTitulo() != null) h.setTitulo(out.getTitulo());
        if (out.getDescripcion() != null) h.setDescripcion(out.getDescripcion());
        if (out.getCategoria() != null) h.setCategoria(out.getCategoria());
        if (out.getLatitud() != null) h.setLatitud(out.getLatitud());
        if (out.getLongitud() != null) h.setLongitud(out.getLongitud());
        if (out.getFechaAcontecimiento() != null) h.setFechaAcontecimiento(out.getFechaAcontecimiento());

        if (out.getContenidoMultimedia() != null) {
            var cm = out.getContenidoMultimedia();
            if (cm.getUrl() == null || cm.getUrl().isBlank()) {
                h.setContenidoMultimedia(null);
            } else {
                h.setContenidoMultimedia(apiMapper.toContenidoMultimedia(cm));
            }
        }


        Hecho actualizado = hechosRepository.save(h);
        return apiMapper.toOutput(actualizado);
    }

    private static String nullToEmpty(String s) { return s == null ? "" : s; }
}
