package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Service.ServicioAgregador;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agregador")
@CrossOrigin("http://localhost:8080")
public class ServicioAgregadorController {

    @Autowired
    private ServicioAgregador servicioAgregador;

    @GetMapping("/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechos() {
        return ResponseEntity.ok(servicioAgregador.agregarHechosDesdeTodasLasFuentes());
    }

    @GetMapping("/refrescar") // Para probar que anda el refresco automatico de colecciones.
    public ResponseEntity<String> refrescarManual() {
        servicioAgregador.refrescarHechosPeriodicamente();
        return ResponseEntity.ok("✔️ Refresco manual ejecutado correctamente");
    }

}
