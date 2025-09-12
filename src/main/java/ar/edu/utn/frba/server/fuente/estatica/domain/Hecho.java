package ar.edu.utn.frba.server.fuente.estatica.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ar.edu.utn.frba.server.fuente.dinamica.domain.Etiqueta;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hecho {
        private Long idHecho;
        private String titulo;
        private String descripcion;
        private String categoria;
        private ContenidoMultimedia contenidoMultimedia;
        private Double latitud;
        private Double longitud;
        private LocalDate fechaAcontecimiento;
        private LocalDate fechaCarga;
        @Builder.Default private List<Etiqueta> etiquetas = new ArrayList<>();
        private boolean eliminado = false;
        private boolean estaSincronizado = false;

        public void marcarComoEliminado() {
                        this.eliminado = true;
                }

        public boolean estaEliminado() {
                        return eliminado;
                }

        public void agregarEtiqueta(Etiqueta etiqueta) {
                        this.etiquetas.add(etiqueta);
                }
}
