package ar.edu.utn.frba.server.servicioAgregador.services;




import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AlgoritmoDeConsensoStrategy;

import java.util.List;

public interface IColeccionService {

    ColeccionOutputDto crearColeccion(ColeccionInputDto dto);
    List<Coleccion> findAll();
    ColeccionOutputDto agregarHechoAColeccion(Long coleccionId, Long hechoId);
    List<Hecho> obtenerHechosPorColeccion(Long idColeccion);
    ColeccionOutputDto coleccionOutputDto(Coleccion coleccion);
    Coleccion crearColeccionDesdeFuentes(String titulo, String criterio);
    void actualizarHechos(List<Hecho> nuevosHechos);
    List<Hecho> navegarHechos(Long coleccionId, String modoNavegacion);
    void setAlgoritmoDeConsenso(Long idColeccion, AlgoritmoDeConsensoStrategy algoritmo);
    HechosOutputDto consensuarHecho(Long coleccionId, Long hechoId);
    Hecho agregarFuenteAHecho(Long coleccionId, Long hechoId, Fuente tipoFuente);
    Hecho quitarFuenteDeHecho(Long coleccionId, Long hechoId);
    List<Hecho> filtrarHechosPorColeccion(Long coleccionId, String titulo, String categoria);
    Coleccion findByIdOrThrow(Long id);
}
