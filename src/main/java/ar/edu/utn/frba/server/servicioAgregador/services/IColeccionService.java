package ar.edu.utn.frba.server.servicioAgregador.services;




import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionInputDto;
import ar.edu.utn.frba.server.servicioAgregador.dtos.ColeccionOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.domain.Coleccion;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import ar.edu.utn.frba.server.servicioAgregador.dtos.HechosOutputDto;
import ar.edu.utn.frba.server.servicioAgregador.algoritmos.consenso.AlgoritmoDeConsensoStrategy;

import java.time.LocalDate;
import java.util.List;

public interface IColeccionService {

    ColeccionOutputDto crearColeccionManual(ColeccionInputDto dto);
    ColeccionOutputDto editarColeccion(Long id, ColeccionInputDto dto);
    void eliminarColeccion(Long id);
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
    Hecho quitarFuenteDeHecho(Long coleccionId, Long hechoId, Fuente tipoFuente);
    List<Hecho> filtrarHechosPorColeccion(Long coleccionId,
                                          String titulo,
                                          String categoria,
                                          LocalDate fechaReporteDesde,
                                          LocalDate fechaReporteHasta,
                                          LocalDate fechaAcontecimientoDesde,
                                          LocalDate fechaAcontecimientoHasta,
                                          Double latitud,
                                          Double longitud);
    Coleccion findByIdOrThrow(Long id);
}
