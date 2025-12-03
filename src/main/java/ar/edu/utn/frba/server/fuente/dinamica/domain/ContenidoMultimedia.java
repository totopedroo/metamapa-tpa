package ar.edu.utn.frba.server.fuente.dinamica.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "contdin")
@Table(name = "contenido_multimedia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContenidoMultimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", length = 2000, nullable = false)
    private String url;
}