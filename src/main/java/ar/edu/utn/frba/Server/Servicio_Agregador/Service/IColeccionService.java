package ar.edu.utn.frba.Server.Servicio_Agregador.Service;




import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.TipoFuente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;

import java.nio.file.Path;
import java.util.List;

public interface IColeccionService {

    public List<Coleccion> findAll();
    ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId);
    public List<Hecho> obtenerHechosPorColeccion(String idColeccion);
    //public ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId);
    public Coleccion setColeccionApi();
   // public Coleccion setColeccionCsv();
    public void actualizarHechos(List<Hecho> nuevosHechos);
    public List<Hecho> navegarHechos(String coleccionId, ModoNavegacionStrategy modoNavegacion);
    public void setAlgoritmoDeConsenso(String idColeccion, AlgoritmoDeConsensoStrategy algoritmo);
    public Coleccion crearColeccionDesdeCSVHardcoded(String nombreColeccion);
    public Coleccion setColeccionCsv();
    public Hecho consensuarHecho(String coleccionId, Long hechoId);
    public Hecho agregarFuenteAHecho(String coleccionId, Long hechoId, TipoFuente tipoFuente);
    public Hecho quitarFuenteDeHecho(String coleccionId, Long hechoId);
    public List<Hecho> filtrarHechosPorColeccion(String coleccionId, String titulo, String categoria);
}
