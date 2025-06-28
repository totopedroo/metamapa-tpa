package ar.edu.utn.frba.Fuente_Proxy.Service.Service;

import ar.edu.utn.frba.Service.IDetectorDeSpam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DetectorDeSpam implements IDetectorDeSpam {

    private static final List<String> PALABRAS_SPAM = List.of("oferta", "urgente", "dinero", "compre ahora");

    @Override
    public boolean esSpam(String texto) {
        String lower = texto.toLowerCase();
        return PALABRAS_SPAM.stream().anyMatch(lower::contains);
    }

    public boolean justificacionRepetida(String justificacion, String justificacionAntigua ){
        return justificacion.equals(justificacionAntigua);
    }
}
