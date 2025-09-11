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
    public HechosOutputDto editarHecho(Long idHecho, HechosOutputDto patch) {
        var hecho = hechosRepository.findById(idHecho);
        if (hecho == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrado");

        // Campos simples (aplico solo si vienen con valor)
        if (patch.getTitulo() != null && !patch.getTitulo().isBlank()) hecho.setTitulo(patch.getTitulo());
        if (patch.getDescripcion() != null) hecho.setDescripcion(patch.getDescripcion());
        if (patch.getCategoria() != null) hecho.setCategoria(patch.getCategoria());
        if (patch.getLatitud() != null) hecho.setLatitud(patch.getLatitud());
        if (patch.getLongitud() != null) hecho.setLongitud(patch.getLongitud());
        if (patch.getFechaAcontecimiento() != null) hecho.setFechaAcontecimiento(patch.getFechaAcontecimiento());

        // Anidados
        if (patch.getContenidoMultimedia() != null) {
            hecho.setContenidoMultimedia(apiMapper.toContenidoMultimedia(patch.getContenidoMultimedia()));
        }
        if (patch.getContribuyente() != null) {
            if (hecho.getContribuyente() == null) {
                hecho.setContribuyente(apiMapper.toContribuyente(patch.getContribuyente()));
            } else {
                var c = hecho.getContribuyente();
                var dto = patch.getContribuyente();
                if (dto.getNombre() != null) c.setNombre(dto.getNombre());
                if (dto.getApellido() != null) c.setApellido(dto.getApellido());
                if (dto.getFechaNacimiento() != null) c.setFechaNacimiento(dto.getFechaNacimiento());
            }
        }

        // Colecciones: reemplazo total
        if (patch.getEtiquetas() != null) {
            hecho.getEtiquetas().clear();
            hecho.getEtiquetas().addAll(patch.getEtiquetas());
        }

        // Guardar y devolver DTO de salida
        hechosRepository.save(hecho);
        return apiMapper.toOutput(hecho);
    }
}