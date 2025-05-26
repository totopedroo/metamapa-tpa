package ar.edu.utn.frba.Service;

import ar.edu.utn.frba.domain.Coleccion;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioColecciones {

    private final List<Coleccion> colecciones = new ArrayList<>();

    public void agregarColeccion(Coleccion coleccion) {
        colecciones.add(coleccion);
    }

    public List<Coleccion> getColecciones() {
        return colecciones;
    }

    public void actualizarHechos(List<Hecho> nuevosHechos) {
        for (Coleccion coleccion : colecciones) {
            for (Hecho hecho : nuevosHechos) {
                if (hecho.estaEliminado()) continue;

                boolean cumple = coleccion.getCriterioDePertenencia()
                        .stream()
                        .allMatch(c -> c.cumple(hecho));

                if (cumple) {
                    coleccion.setHecho(hecho);
                }
            }
        }
    }
}
