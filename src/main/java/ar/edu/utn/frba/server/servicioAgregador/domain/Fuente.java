package ar.edu.utn.frba.server.servicioAgregador.domain;

import ar.edu.utn.frba.server.common.enums.TipoFuente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fuente")
public class Fuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "tipo_fuente")
    @Enumerated(EnumType.STRING)
    private TipoFuente tipo;

    // Constructores útiles
    public Fuente(String path, TipoFuente tipoFuente) {
        this.path = path;
        this.tipo = tipoFuente;
    }

    public Fuente(TipoFuente tipo) {
        this.tipo = tipo;
    }
}
