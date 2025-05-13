package Service.Impl;

import Dtos.HechosOutputDto;
import Repository.IHechosRepository;
import Repository.Implementacion.HechosRepository;
import Service.IHechosService;
import domain.Hecho;

import java.util.List;

public class HechosService implements IHechosService {
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
