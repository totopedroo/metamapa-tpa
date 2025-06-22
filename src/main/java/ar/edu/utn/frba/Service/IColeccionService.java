package ar.edu.utn.frba.Service;




import ar.edu.utn.frba.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.domain.Coleccion;
import ar.edu.utn.frba.domain.Hecho;

import java.util.List;

public interface IColeccionService {

    public List<ColeccionOutputDto> buscarTodos();

    public List<HechosOutputDto> obtenerHechosPorColeccion(String idColeccion);
    public ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId);
    public Coleccion setColeccionApi();
    //public Coleccion setColeccionCsv();
    public void actualizarHechos(List<Hecho> nuevosHechos);
}
