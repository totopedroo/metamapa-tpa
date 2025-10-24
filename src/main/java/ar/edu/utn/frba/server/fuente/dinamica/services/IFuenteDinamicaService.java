package ar.edu.utn.frba.server.fuente.dinamica.services;

import ar.edu.utn.frba.server.fuente.dinamica.domain.Hecho;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosInputDto;
import ar.edu.utn.frba.server.fuente.dinamica.dtos.HechosOutputDto;

public interface IFuenteDinamicaService {
    /**
     * Crea un nuevo hecho a través de un contribuyente

     * @param inputDto Los datos del hecho a crear
     * @return El hecho creado
     */
    HechosOutputDto crearHecho(HechosInputDto inputDto);

    /**
     * Edita un hecho existente
     *
     * @param idHecho  ID del hecho a editar
     * @param inputDto Los nuevos datos del hecho
     * @return El hecho editado o null si no se pudo editar
     */
    HechosOutputDto editarHecho(Long idHecho, HechosOutputDto outputDto);
} 