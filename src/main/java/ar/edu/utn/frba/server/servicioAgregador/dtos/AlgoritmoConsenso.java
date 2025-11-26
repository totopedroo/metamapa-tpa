package ar.edu.utn.frba.server.servicioAgregador.dtos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "algoritmo_consenso")
@Data
@NoArgsConstructor
public class AlgoritmoConsenso {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_algoritmo_consenso")
  private Long id;

  private String nombre;
}