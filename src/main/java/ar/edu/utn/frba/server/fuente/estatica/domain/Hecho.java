package ar.edu.utn.frba.server.fuente.estatica.domain;


import ar.edu.utn.frba.server.contratos.enums.EstadoConsenso;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;

import ar.edu.utn.frba.server.fuente.estatica.domain.ContenidoMultimedia;
import ar.edu.utn.frba.server.servicioAgregador.domain.Fuente;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.Name;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity(name = "hecho_est")
@Table(name = "hecho")
@Access(AccessType.FIELD)

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Hecho {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idHecho;

        @Column(name = "titulo", length = 255)
        private String titulo;

        @Column(name = "descripcion", columnDefinition = "TEXT")
        private String descripcion;

        @Column(name = "categoria", length = 100)
        private String categoria;

        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JoinColumn(name = "contenido_multimedia_id", referencedColumnName = "id", nullable = true)
        private ContenidoMultimedia contenidoMultimedia;

        @Column(name = "latitud")
        private Double latitud;

        @Column(name = "longitud")
        private Double longitud;

        @Column(name = "provincia", length = 100)
        private String provincia;

        @Column(name = "fecha_acontecimiento")
        private LocalDate fechaAcontecimiento;

        @Column(name = "hora_acontecimiento")
        private LocalTime horaAcontecimiento;

        @Column(name = "fecha_carga")
        private LocalDate fechaCarga;



        @Column(name = "eliminado", nullable = false)
        @Builder.Default
        private boolean eliminado = false;

        @Column(name = "consensuado")
        @Builder.Default
        private Boolean consensuado = false;

        // Si manejás una relación con Fuente, podés dejar ManyToMany / ManyToOne según tu modelo:
        @ManyToMany
        @JoinTable(
                name = "hecho_fuente",
                joinColumns = @JoinColumn(name = "hecho_id"),
                inverseJoinColumns = @JoinColumn(name = "fuente_id")
        )
        @Builder.Default
        private List<Fuente> fuente = new ArrayList<>();


        // Helpers
        public Optional<ContenidoMultimedia> getContenidoMultimediaOpt() {
                return Optional.ofNullable(contenidoMultimedia);
        }

        public void setId(Long idHecho) {
                this.idHecho = idHecho;
        }

        public boolean estaEliminado() {
                return eliminado;
        }
}
