package ar.edu.utn.frba.Servicio_Agregador.Service.Consenso;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Domain.TipoFuente;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AbsolutaStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.ConsensoPorDefectoStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.MayoriaSimpleStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.MultiplesMencionesStrategy;
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
        @JsonSubTypes.Type(value = AbsolutaStrategy.class, name = "absoluta"),
        @JsonSubTypes.Type(value = ConsensoPorDefectoStrategy.class, name = "defecto")
})


public interface AlgoritmoDeConsensoStrategy {
    boolean tieneConsenso(Hecho hecho, List<Hecho> todosLosHechos);


}
