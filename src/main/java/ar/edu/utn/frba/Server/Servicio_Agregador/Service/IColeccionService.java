package ar.edu.utn.frba.Server.Servicio_Agregador.Service;




import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Fuente;
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
    ColeccionOutputDto agregarHechoAColeccion(Long coleccionId, Long hechoId);
    public List<Hecho> obtenerHechosPorColeccion(Long idColeccion);
    //public ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId);
    public Coleccion setColeccionApi();
   // public Coleccion setColeccionCsv();
    public void actualizarHechos(List<Hecho> nuevosHechos);
    public List<Hecho> navegarHechos(Long coleccionId, ModoNavegacionStrategy modoNavegacion);
    public void setAlgoritmoDeConsenso(Long idColeccion, AlgoritmoDeConsensoStrategy algoritmo);
    public Coleccion crearColeccionDesdeCSVHardcoded(String nombreColeccion);
    public Coleccion setColeccionCsv(String archivoCsvStream);
    public Hecho consensuarHecho(Long coleccionId, Long hechoId);
    public Hecho agregarFuenteAHecho(Long coleccionId, Long hechoId, Fuente fuente);
    public Hecho quitarFuenteDeHecho(Long coleccionId, Long hechoId);
    public List<Hecho> filtrarHechosPorColeccion(Long coleccionId, String titulo, String categoria);
}
