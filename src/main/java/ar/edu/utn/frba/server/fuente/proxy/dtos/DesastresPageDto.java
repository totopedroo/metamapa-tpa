package ar.edu.utn.frba.server.fuente.proxy.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class DesastresPageDto {
    @JsonProperty("current_page") private int currentPage;
    private List<DesastreDto> data;
    @JsonProperty("next_page_url") private String nextPageUrl;
}
