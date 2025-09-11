package ar.edu.utn.frba.server.servicioAgregador.services;




import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
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
    ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId);
    List<Hecho> obtenerHechosPorColeccion(String idColeccion);
    ColeccionOutputDto coleccionOutputDto(Coleccion coleccion);
    Coleccion crearColeccionDesdeFuentes(String titulo, String criterio);
    void actualizarHechos(List<Hecho> nuevosHechos);
    List<Hecho> navegarHechos(String coleccionId, String modoNavegacion);
    void setAlgoritmoDeConsenso(String idColeccion, AlgoritmoDeConsensoStrategy algoritmo);
    HechosOutputDto consensuarHecho(String coleccionId, Long hechoId);
    Hecho agregarFuenteAHecho(String coleccionId, Long hechoId, TipoFuente tipoFuente);
    Hecho quitarFuenteDeHecho(String coleccionId, Long hechoId);
    List<Hecho> filtrarHechosPorColeccion(String coleccionId, String titulo, String categoria);
    Coleccion findByIdOrThrow(String id);
}
