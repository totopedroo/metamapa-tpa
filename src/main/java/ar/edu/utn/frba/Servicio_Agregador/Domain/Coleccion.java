package ar.edu.utn.frba.Servicio_Agregador.Domain;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.ModoNavegacionStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Domain.CriterioDePertenencia;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.domain.Visualizador;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
@Setter
public class Coleccion {
    public String id;
    private List<Hecho> hechos;
    public String titulo;
    public String descripcion;
    public  List<CriterioDePertenencia> criterioDePertenencia;
    private AlgoritmoDeConsensoStrategy algoritmoDeConsenso;
    private List<Hecho> hechosConsensuados = new ArrayList<>();
    private ModoNavegacionStrategy modoNavegacion;


    public Coleccion(String id, String titulo, String descripcion, List<CriterioDePertenencia> criterioDePertenencia) {
        this.hechos = new ArrayList<>();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterioDePertenencia = criterioDePertenencia;
    }

    public List<Hecho> getHechosVisibles() {
        return hechos.stream()
            .filter(h -> !h.estaEliminado())
            .toList();
    } //REVISAR CON GETHECHOSFILTRADOS, SE PUEDE BORRAR UNA

    public void setHecho(Hecho hecho) {
        if (hecho.estaEliminado()) {
            System.out.println("No se puede agregar el hecho '" + hecho.getTitulo() + "' porque fue eliminado.");
            return;
        }

        this.hechos.add(hecho);
        System.out.println("Hecho agregado a la colección: " + hecho.getTitulo());
    }

    public void setCriterioDePertenencia(List<CriterioDePertenencia> criterioDePertenencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

    public void getHechosFiltrados(Coleccion coleccion, Visualizador visualizador) {
        System.out.println("Hechos visibles para " + visualizador.nombre + " en colección '" + coleccion.getTitulo() + "':");
        for (Hecho hecho : coleccion.getHechos()) {
            boolean cumple = visualizador.filtrosPersonales.stream().allMatch(c -> c.cumple(hecho));
            if (cumple) {
                System.out.println("-> " + hecho.getTitulo());
            }
        }
    }

    public void setCriterioDePertenencia(CriterioDePertenencia criterio) {
        criterioDePertenencia.add(criterio);
    }



}