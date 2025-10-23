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
@Table(name = "coleccion")
public class Coleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToMany correctamente definida
    @ManyToMany
    @JoinTable(
            name = "coleccion_por_hecho",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> hechos = new ArrayList<>();

    @Column(name = "titulo", columnDefinition = "CHAR(50)")
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    private Administrador administrador;

    // Relación ManyToMany sin conflicto
    @ManyToMany
    @JoinTable(
            name = "criterios_por_coleccion",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "criterio_id")
    )
    private List<CriterioDePertenencia> criteriosDePertenencia = new ArrayList<>();

    @Transient
    private AlgoritmoDeConsensoStrategy algoritmoDeConsenso;

    @Transient
    private List<Hecho> hechosConsensuados = new ArrayList<>();

    @Transient
    private ModoNavegacionStrategy modoNavegacion;

    @Column(name = "eliminada", nullable = false)
    private boolean eliminada = false;

    // Constructor útil
    public Coleccion(String titulo, String descripcion, List<CriterioDePertenencia> criterios) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criteriosDePertenencia = criterios != null ? criterios : new ArrayList<>();
        this.hechos = new ArrayList<>();
    }

    // Métodos de negocio
    public List<Hecho> getHechosVisibles() {
        return hechos.stream()
                .filter(h -> !h.estaEliminado())
                .toList();
    }

    public void agregarHecho(Hecho hecho) {
        if (hecho.estaEliminado()) {
            System.out.println("No se puede agregar el hecho '" + hecho.getTitulo() + "' porque fue eliminado.");
            return;
        }
        this.hechos.add(hecho);
        System.out.println("Hecho agregado a la colección: " + hecho.getTitulo());
    }

    public void agregarCriterio(CriterioDePertenencia criterio) {
        this.criteriosDePertenencia.add(criterio);
    }

    public void marcarComoEliminada() {
        this.eliminada = true;
    }

    public boolean estaEliminada() {
        return eliminada;
    }
}
