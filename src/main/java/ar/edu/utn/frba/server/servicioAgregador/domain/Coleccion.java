package ar.edu.utn.frba.server.servicioAgregador.domain;

import ar.edu.utn.frba.server.domain.Visualizador;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.server.servicioAgregador.domain.navegacion.ModoNavegacionStrategy;
import jakarta.persistence.*;
import lombok.*;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coleccion") // en tus logs Hibernate trabaja con 'coleccion'
public class Coleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    @JoinTable(
            name = "coleccion_por_hecho",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> hechos = new ArrayList<>();

    @Column(name = "titulo", columnDefinition = "Char(50)")
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "administrador_id", nullable = false) // dejar nullable si la FK en DB lo permite
    private Administrador administrador;

    @ManyToMany
    @JoinTable(
            name = "criterios_por_coleccion",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "criterio_id")
    )
    private List<CriterioDePertenencia> criterioDePertenencia = new ArrayList<>();

    /* ---- Campos no persistidos (lógica de dominio) ---- */
    @Transient
    private AlgoritmoDeConsensoStrategy algoritmoDeConsenso;

    @Transient
    private List<Hecho> hechosConsensuados = new ArrayList<>();

    @Transient
    private ModoNavegacionStrategy modoNavegacion;

    /* ---- Constructores de conveniencia ---- */
    public Coleccion(String titulo, String descripcion, List<CriterioDePertenencia> criterios) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        if (criterios != null) this.criterioDePertenencia = new ArrayList<>(criterios);
    }

    /* ---- Helpers de dominio ---- */
    public List<Hecho> getHechosVisibles() {
        return this.hechos.stream()
                .filter(h -> !h.estaEliminado())
                .toList();
    }

    /** Agrega un hecho si no está eliminado */
    public void setHecho(Hecho hecho) {
        if (hecho == null) return;
        if (hecho.estaEliminado()) return;
        this.hechos.add(hecho);
    }

    public void addCriterio(CriterioDePertenencia criterio) {
        if (criterio == null) return;
        this.criterioDePertenencia.add(criterio);
    }

    /** Muestra por consola los hechos que pasan los filtros personales del visualizador */
    public void getHechosFiltrados(Coleccion coleccion, Visualizador visualizador) {
        System.out.println("Hechos visibles para " + visualizador.nombre + " en colección '" + coleccion.getTitulo() + "':");
        for (Hecho hecho : coleccion.getHechos()) {
            boolean cumple = visualizador.filtrosPersonales.stream().allMatch(c -> c.cumple(hecho));
            if (cumple) System.out.println("-> " + hecho.getTitulo());
        }
    }
}
