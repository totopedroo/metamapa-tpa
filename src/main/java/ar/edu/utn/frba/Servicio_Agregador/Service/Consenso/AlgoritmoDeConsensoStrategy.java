package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;

import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MayoriaSimpleStrategy.class, name = "mayoriaSimple"),
        @JsonSubTypes.Type(value = MultiplesMencionesStrategy.class, name = "multiplesMenciones"),
        @JsonSubTypes.Type(value = AbsolutaStrategy.class, name = "absoluta")
})

public interface AlgoritmoDeConsensoStrategy {
    List<Hecho> obtenerHechosConsensuados(List<List<Hecho>> hechosPorFuente);
}
