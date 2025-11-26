package ar.edu.utn.frba.server.servicioAgregador.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * DTO simplificado para mostrar en la Landing Page (Frontend público).
 * Solo contiene la información esencial para las tarjetas de vista previa.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private String lugar;    // En tu entidad esto se mapea desde 'provincia'
  private LocalDate fecha; // En tu entidad esto se mapea desde 'fechaAcontecimiento'
}