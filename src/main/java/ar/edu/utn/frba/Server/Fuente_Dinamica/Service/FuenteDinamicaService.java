package ar.edu.utn.frba.Server.Fuente_Dinamica.Service;

import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosInputDto;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Repository.IHechosRepository;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {

  /*  @Autowired
    private FuenteDinamica fuenteDinamica;
*/
    @Autowired
    private IHechosRepository hechosRepository;

    @Override
    public HechosOutputDto crearHecho( HechosInputDto inputDto) {
        Hecho hecho = new Hecho(
                inputDto.getTitulo(),
                inputDto.getDescripcion(),
                inputDto.getCategoria(),
                inputDto.getContenidoMultimedia().orElse(null),
                inputDto.getLatitud(),
                inputDto.getLongitud(),
                inputDto.getFechaAcontecimiento(),
                inputDto.getFechaCarga(),
                inputDto.getIdHecho()
        );


        hecho.setContribuyente(inputDto.getContribuyente());

        return new HechosOutputDto(

                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getContenidoMultimedia(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaAcontecimiento(),
                hecho.getFechaCarga(),
                hecho.getContribuyente(),
                hecho.getIdHecho()
        );
    }

    //@Override
   public HechosOutputDto editarHecho( Long idHecho, HechosOutputDto outputDto) {
        Hecho hecho = hechosRepository.findById(idHecho);
        if (hecho == null) {
            return null;
        }

      /*  boolean editado = editarHecho(
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
*/
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
