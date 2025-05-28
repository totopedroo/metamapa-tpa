package ar.edu.utn.frba.Service.Impl;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.Service.IFuenteDinamicaService;
import ar.edu.utn.frba.domain.Contribuyente;
import ar.edu.utn.frba.domain.FuenteDinamica;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {

    @Autowired
    private FuenteDinamica fuenteDinamica;

    @Autowired
    private IHechosRepository hechosRepository;

    @Override
    public HechosOutputDto crearHecho( HechosInputDto inputDto) {
        Hecho hecho = fuenteDinamica.crearHecho(
                inputDto.getContribuyente(),
                inputDto.getTitulo(),
                inputDto.getDescripcion(),
                inputDto.getCategoria(),
                inputDto.getContenidoMultimedia().orElse(null),
                inputDto.getLatitud(),
                inputDto.getLongitud(),
                inputDto.getFechaAcontecimiento()
        );

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

    @Override
    public HechosOutputDto editarHecho( Long idHecho, HechosOutputDto outputDto) {
        Hecho hecho = hechosRepository.findById(idHecho);
        if (hecho == null) {
            return null;
        }

        boolean editado = fuenteDinamica.editarHecho(
                outputDto.getContribuyente(),
                hecho,
                outputDto.getTitulo(),
                outputDto.getDescripcion(),
                outputDto.getCategoria(),
                outputDto.getContenidoMultimedia().orElse(null)
        );

        if (!editado) {
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