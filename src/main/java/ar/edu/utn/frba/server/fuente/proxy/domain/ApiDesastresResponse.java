package ar.edu.utn.frba.server.fuente.proxy.domain;

import ar.edu.utn.frba.server.fuente.proxy.dtos.DesastreDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiDesastresResponse {
  private int currentPage;            // mapea current_page
  private List<DesastreDto> data;
}
