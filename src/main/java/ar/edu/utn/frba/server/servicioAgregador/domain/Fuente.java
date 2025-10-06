package ar.edu.utn.frba.server.servicioAgregador.domain;
import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Fuente")
public class Fuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="path")
    private String path;
    @Column(name="tipo_fuente")
    @Enumerated(EnumType.STRING)
    private TipoFuente tipo;


    public Fuente(String url, TipoFuente tipoFuente) {
    }

    public Fuente(TipoFuente tipo) {}


}