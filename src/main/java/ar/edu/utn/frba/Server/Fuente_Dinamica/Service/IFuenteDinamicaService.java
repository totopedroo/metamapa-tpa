package ar.edu.utn.frba.Server.Fuente_Dinamica.Service;

import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosInputDto;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosOutputDto;

public interface IFuenteDinamicaService {
    /**
     * Crea un nuevo hecho a través de un contribuyente

     * @param inputDto Los datos del hecho a crear
     * @return El hecho creado
     */
    HechosOutputDto crearHecho(HechosInputDto inputDto);

    /**
     * Edita un hecho existente

     * @param idHecho ID del hecho a editar
     * @param inputDto Los nuevos datos del hecho
     * @return El hecho editado o null si no se pudo editar
     */
    HechosOutputDto editarHecho( Long idHecho, HechosOutputDto outputDto);
} 