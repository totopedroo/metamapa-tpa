package ar.edu.utn.frba.server.servicioAgregador.services;




import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.*;
import ar.edu.utn.frba.server.servicioAgregador.dtos.*;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AlgoritmoDeConsensoStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface IColeccionService {

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
    public Coleccion createColeccionDesdeApi(String nombre) ;
    public ColeccionOutputBD listar(Long id);
    public ColeccionOutputBD editar(Long coleccionId, ColeccionUpdateBD in);
    public ColeccionOutputBD crear(ColeccionInputBD dto);
    public void eliminar(Long coleccionId);

    List<ColeccionOutputBD> listarUltimas();

}
