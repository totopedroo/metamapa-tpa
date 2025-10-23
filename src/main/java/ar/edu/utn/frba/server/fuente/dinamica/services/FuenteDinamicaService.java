package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.fuente.dinamica.services.ApiDinamicaMapper;
import ar.edu.utn.frba.server.fuente.dinamica.repositories.IHechosDinamicosRepository;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FuenteDinamicaService implements IFuenteDinamicaService {

    private final IHechosDinamicosRepository hechosRepository;
    private final ApiDinamicaMapper apiMapper; // tu mapper

    @Override
    @Transactional
    public HechosOutputDto crearHecho(HechosInputDto inputDto) {
        // NO seteamos idHecho: lo genera la DB (GenerationType.IDENTITY)
        Hecho hecho = Hecho.builder()
                .titulo(inputDto.getTitulo())
                .descripcion(inputDto.getDescripcion())
                .categoria(inputDto.getCategoria())
             //   .contenidoMultimedia(apiMapper.toContenidoMultimedia(inputDto.getContenidoMultimedia())) // puede ser null
                .latitud(inputDto.getLatitud())
                .longitud(inputDto.getLongitud())

                .fechaAcontecimiento(inputDto.getFechaAcontecimiento())

                .fechaCarga(LocalDate.now())
                .build();


        Hecho guardado = hechosRepository.save(hecho);
        return apiMapper.toOutput(guardado);
    }
    public ApiDinamicaMapper getApiMapper() { return apiMapper; }

    @Override
    @Transactional
    public HechosOutputDto editarHecho(Long id, HechosOutputDto out) {
        Hecho h = hechosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hecho no encontrado: " + id));

        // Patch de campos
        if (out.getTitulo() != null) h.setTitulo(out.getTitulo());
        if (out.getDescripcion() != null) h.setDescripcion(out.getDescripcion());
        if (out.getCategoria() != null) h.setCategoria(out.getCategoria());
        if (out.getLatitud() != null) h.setLatitud(out.getLatitud());
        if (out.getLongitud() != null) h.setLongitud(out.getLongitud());

        if (out.getFechaAcontecimiento() != null) h.setFechaAcontecimiento(out.getFechaAcontecimiento());

       /* if (out.getContenidoMultimedia() != null) {
            h.setContenidoMultimedia(apiMapper.toContenidoMultimedia(out.getContenidoMultimedia())); // recuerda: solo url
        }*/

        Hecho actualizado = hechosRepository.save(h);
        return apiMapper.toOutput(actualizado);
    }
}