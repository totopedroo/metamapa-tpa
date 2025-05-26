package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Service.ServicioColecciones;
import ar.edu.utn.frba.domain.Coleccion;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colecciones")
@CrossOrigin("http://localhost:8080")
public class ColeccionesController {

    @Autowired
    private ServicioColecciones servicioColecciones;

    // ✅ Obtener todas las colecciones
    @GetMapping
    public List<Coleccion> obtenerTodasLasColecciones() {
        return servicioColecciones.getColecciones();
    }

    // ✅ Obtener los hechos visibles (no eliminados) de una colección específica
    @GetMapping("/{titulo}/hechos")
    public List<Hecho> obtenerHechosDeColeccion(@PathVariable String titulo) {
        return servicioColecciones.getColecciones().stream()
                .filter(c -> c.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .map(Coleccion::getHechosVisibles)
                .orElse(List.of());
    }
}
