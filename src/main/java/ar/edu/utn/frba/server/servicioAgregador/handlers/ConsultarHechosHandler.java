package ar.edu.utn.frba.server.servicioAgregador.handlers;

import ar.edu.utn.frba.server.contratos.fuentes.FuentePort;
import ar.edu.utn.frba.server.contratos.dtos.HechoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarHechosHandler {

    private final List<FuentePort> fuentes; // Spring inyecta todos los beans FuentePort

    /** Importa de todas las fuentes y devuelve la lista fusionada/filtrada. */
    public List<HechoDto> consultar(String criterio) {
        var todos = fuentes.stream()
                .flatMap(f -> f.importar(criterio).stream())
                .toList();

        return fusionarDuplicados(todos);
    }

    /** Agrupa por clave “casi igual”: título normalizado + fecha + coords redondeadas. */
    private List<HechoDto> fusionarDuplicados(List<HechoDto> entrada) {
        record Key(String titulo, LocalDate fecha, long latBin, long lonBin) {}
        var grupos = entrada.stream().collect(Collectors.groupingBy(h ->
                new Key(
                        normalizar(h.titulo()),
                        h.fecha(),
                        bin(h, true),   // lat
                        bin(h, false)   // lon
                )
        ));

        // estrategia simple: devolvés uno por grupo (el primero) y guardás los orígenes para consenso en tu dominio
        return grupos.values().stream()
                .map(lista -> lista.get(0))
                .toList();
    }

    private static String normalizar(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase().trim();
    }

    private static long bin(HechoDto h, boolean lat) {
        // ~100m: 3 decimales; ajustá si querés más precisión
        Double v = lat ? h.latitud() : h.longitud();
        if (v == null) return 0;
        return Math.round(v * 1000);
    }
}
