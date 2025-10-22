package ar.edu.utn.frba.server.servicioAgregador.algoritmos.consenso;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
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
        @JsonSubTypes.Type(value = AbsolutaStrategy.class, name = "absoluta"),
        @JsonSubTypes.Type(value = ConsensoPorDefectoStrategy.class, name = "defecto")
})


public interface AlgoritmoDeConsensoStrategy {
    String codigo(); // "mayoriaSimple", "multiplesMenciones", "absoluta", "defecto"
    ConsensoResult evaluar(Hecho hecho, List<Hecho> todosLosHechos);
}
