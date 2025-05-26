package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.domain.Contribuyente;

public interface IFuenteDinamicaService {
    /**
     * Crea un nuevo hecho a través de un contribuyente
     * @param contribuyente El contribuyente que crea el hecho (puede ser anónimo)
     * @param inputDto Los datos del hecho a crear
     * @return El hecho creado
     */
    HechosOutputDto crearHecho(Contribuyente contribuyente, HechosInputDto inputDto);

    /**
     * Edita un hecho existente
     * @param contribuyente El contribuyente que quiere editar el hecho
     * @param idHecho ID del hecho a editar
     * @param inputDto Los nuevos datos del hecho
     * @return El hecho editado o null si no se pudo editar
     */
    HechosOutputDto editarHecho(Contribuyente contribuyente, Long idHecho, HechosInputDto inputDto);
} 