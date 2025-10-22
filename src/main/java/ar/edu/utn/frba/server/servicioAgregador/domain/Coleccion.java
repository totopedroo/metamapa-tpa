package ar.edu.utn.frba.server.servicioAgregador.domain;
import ar.edu.utn.frba.server.servicioAgregador.algoritmos.consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.server.servicioAgregador.algoritmos.navegacion.ModoNavegacionStrategy;
import ar.edu.utn.frba.server.domain.Visualizador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Coleccion")
public class Coleccion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;
    @ManyToMany
    @JoinTable(name = "coleccion_por_hecho", joinColumns = @JoinColumn(name = "coleccion_id"), inverseJoinColumns = @JoinColumn(name = "hecho_id"))
    private List<Hecho> hechos;
    @Column(name="Titulo", columnDefinition = "Char(50)")
    public String titulo;
    @Column(name="descripcion", columnDefinition = "TEXT")
    public String descripcion;
    @ManyToOne
    @JoinColumn(name = "administrador_id")
    private Administrador administrador;
    @ManyToMany@JoinTable(name = "criterios_por_coleccion", joinColumns = @JoinColumn(name = "coleccion_id"), inverseJoinColumns = @JoinColumn(name = "criterio_id"))
    public  List<CriterioDePertenencia> criterioDePertenencia;
    @Transient
    private AlgoritmoDeConsensoStrategy algoritmoDeConsenso;
    @Transient
    private List<Hecho> hechosConsensuados = new ArrayList<>();
    @Transient
    private ModoNavegacionStrategy modoNavegacion;
    @Column(name = "eliminada", nullable = false)
    private boolean eliminada = false;


    public Coleccion( String titulo, String descripcion, List<CriterioDePertenencia> criterioDePertenencia) {

        this.hechos = new ArrayList<>();
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

    public void marcarComoEliminada() {
        this.eliminada = true;
    }

    public boolean estaEliminada() {
        return eliminada;
    }
}