package ar.edu.utn.frba.Service.Impl;

import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.Service.IHechosService;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HechosService implements IHechosService {
@Autowired
    private IHechosRepository hechosRepository;
    @Override
    public List<HechosOutputDto> buscarTodos() {
        List<Hecho> hechos = hechosRepository.findAll();
        return hechos.stream().map(this::hechoOutputDto).toList();
    }

    public HechosOutputDto hechoOutputDto(Hecho hecho) {
        var dto = new HechosOutputDto();
        dto.setIdHecho(hecho.getIdHecho());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setContenidoMultimedia(hecho.getContenidoMultimedia());
        dto.setLongitud(hecho.getLongitud());
        dto.setLatitud(hecho.getLatitud());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        dto.setFechaCarga(hecho.getFechaCarga());
        dto.setEtiquetas(hecho.getEtiquetas());
        dto.setSolicitudes(hecho.getSolicitudes());
        return dto;
    }
}
