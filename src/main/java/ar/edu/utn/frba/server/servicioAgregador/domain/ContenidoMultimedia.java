package ar.edu.utn.frba.server.servicioAgregador.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="contenido_multimedia")
public class ContenidoMultimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="url", columnDefinition = "VARCHAR(100)")
    public String url;
    @OneToOne(mappedBy = "contenidoMultimedia")
    private Hecho hechoAsociado;
}