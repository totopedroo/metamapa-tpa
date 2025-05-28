package ar.edu.utn.frba.Service.Impl;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.Service.IHechosService;
import ar.edu.utn.frba.domain.Contribuyente;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {

    @Autowired
    private IHechosRepository hechosRepository;

    @Override
    public List<HechosOutputDto> buscarTodos() {
        List<Hecho> hechos = hechosRepository.findAll();
        return hechos.stream()
                .map(hecho -> new HechosOutputDto(
                        hecho.getIdHecho(),
                        hecho.getTitulo(),
                        hecho.getDescripcion(),
                        hecho.getCategoria(),
                        hecho.getContenidoMultimedia(),
                        hecho.getLatitud(),
                        hecho.getLongitud(),
                        hecho.getFechaAcontecimiento(),
                        hecho.getFechaCarga(),
                        hecho.getEtiquetas(),
                        hecho.getSolicitudes(),
                        hecho.getContribuyente()
                ))
                .collect(Collectors.toList());
    }

    public HechosOutputDto crearHecho(Contribuyente contribuyente, HechosInputDto inputDto) {
        Hecho hecho = new Hecho(
                inputDto.getTitulo(),
                inputDto.getDescripcion(),
                inputDto.getCategoria(),
                inputDto.getContenidoMultimedia().orElse(null),
                inputDto.getLatitud(),
                inputDto.getLongitud(),
                inputDto.getFechaAcontecimiento(),
                LocalDate.now(),
                System.currentTimeMillis()
        );

        hechosRepository.save(hecho);

        return new HechosOutputDto(
                hecho.getIdHecho(),
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getContenidoMultimedia(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaAcontecimiento(),
                hecho.getFechaCarga(),
                hecho.getEtiquetas(),
                hecho.getSolicitudes(),
                hecho.getContribuyente()
        );
    }

    public HechosOutputDto obtenerHecho(Long id) {
        Hecho hecho = hechosRepository.findById(id);
        if (hecho == null) {
            return null;
        }

        return new HechosOutputDto(
                hecho.getIdHecho(),
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getContenidoMultimedia(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaAcontecimiento(),
                hecho.getFechaCarga(),
                hecho.getEtiquetas(),
                hecho.getSolicitudes(),
                hecho.getContribuyente()
        );
    }
}
