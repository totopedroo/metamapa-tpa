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
@Table(name="Etiqueta")
public class Etiqueta
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column
    public String etiqueta;

    public Etiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
