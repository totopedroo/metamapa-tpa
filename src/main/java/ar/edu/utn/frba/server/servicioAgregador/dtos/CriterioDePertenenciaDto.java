package ar.edu.utn.frba.server.servicioAgregador.dtos;

import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CriterioDePertenenciaDto {

    private Long id_criterio;
    private String columna;
    private String tipo;      // "texto" | "fecha"
    private String valor;     // si tipo="texto"

    @JsonFormat(pattern = "yyyy-MM-dd")  // opcional para formato consistente
    private LocalDate desde;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hasta;

    public static CriterioDePertenenciaDto fromModel(CriterioDePertenencia c) {
        CriterioDePertenenciaDto dto = new CriterioDePertenenciaDto();
        if (c == null) return dto;
        dto.setId_criterio(c.getId_criterio());
        dto.setColumna(c.columna);
        dto.setTipo(c.tipo);
        dto.setValor(c.valor);
        dto.setDesde(c.desde);
        dto.setHasta(c.hasta);
        return dto;
    }
}