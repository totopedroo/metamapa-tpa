package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.repositories.IHechosRepository;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FuenteDinamicaService implements IFuenteDinamicaService {

    private final IHechosRepository hechosRepository;
    private final ApiDinamicaMapper apiMapper;  // mapper de DTOs internos de la fuente

    @Override
    public HechosOutputDto crearHecho(HechosInputDto inputDto) {
        long idHecho = Math.abs(new SecureRandom().nextLong());

        // Construcción con builder
        Hecho hecho = Hecho.builder()
                .idHecho(idHecho)
                .titulo(inputDto.getTitulo())
                .descripcion(inputDto.getDescripcion())
                .categoria(inputDto.getCategoria())
                .contenidoMultimedia(apiMapper.toContenidoMultimedia(inputDto.getContenidoMultimedia()))
                .latitud(inputDto.getLatitud())
                .longitud(inputDto.getLongitud())
                .fechaAcontecimiento(inputDto.getFechaAcontecimiento())
                .fechaCarga(LocalDate.now())
                .build();

        hecho.setContribuyente(apiMapper.toContribuyente(inputDto.getContribuyente()));

        hechosRepository.save(hecho);
        return apiMapper.toOutput(hecho);
    }

    @Override
    public HechosOutputDto editarHecho(Long id, HechosOutputDto out) {
        Hecho h = hechosRepository.findById(id);
        if (h == null) throw new IllegalArgumentException("Hecho no encontrado: " + id);

        if (out.getTitulo() != null) h.setTitulo(out.getTitulo());
        if (out.getDescripcion() != null) h.setDescripcion(out.getDescripcion());
        if (out.getCategoria() != null) h.setCategoria(out.getCategoria());
        if (out.getLatitud() != null) h.setLatitud(out.getLatitud());
        if (out.getLongitud() != null) h.setLongitud(out.getLongitud());
        if (out.getFechaAcontecimiento() != null) h.setFechaAcontecimiento(out.getFechaAcontecimiento());

        if (out.getContenidoMultimedia() != null)
            h.setContenidoMultimedia(apiMapper.toContenidoMultimedia(out.getContenidoMultimedia()));
        if (out.getContribuyente() != null)
            h.setContribuyente(apiMapper.toContribuyente(out.getContribuyente()));

        hechosRepository.save(h);
        return apiMapper.toOutput(h);
    }
}