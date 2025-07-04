package ar.edu.utn.frba.Servicio_Agregador.Service;




import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;

import java.util.List;

public interface IColeccionService {

    public List<ColeccionOutputDto> buscarTodos();

    ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId);

    public List<Hecho> obtenerHechosPorColeccion(String idColeccion);
    //public ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId);
    public Coleccion setColeccionApi();
   // public Coleccion setColeccionCsv();
    public void actualizarHechos(List<Hecho> nuevosHechos);
    public List<Hecho> navegarHechos(String coleccionId, ModoNavegacionStrategy modoNavegacion);
    public void setAlgoritmoDeConsenso(String idColeccion, AlgoritmoDeConsensoStrategy algoritmo);


    Coleccion setColeccionCsv();
}
