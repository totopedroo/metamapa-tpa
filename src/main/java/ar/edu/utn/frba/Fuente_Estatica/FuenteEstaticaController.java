package ar.edu.utn.frba.Fuente_Estatica;

import ar.edu.utn.frba.Fuente_Estatica.Domain.Hecho;
import ar.edu.utn.frba.Fuente_Estatica.Service.FuenteEstaticaService;
import ar.edu.utn.frba.Fuente_Estatica.Service.IFuenteEstaticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fuente-estatica")
@CrossOrigin("http://localhost:8080")
public class FuenteEstaticaController {

    @Autowired
    private IFuenteEstaticaService fuenteEstaticaService;

    public FuenteEstaticaController() {
        this.fuenteEstaticaService = new FuenteEstaticaService();
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechos() {
        try {
            List<Hecho> hechos = fuenteEstaticaService.sincronizar();
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/importar-csv")
    public ResponseEntity<String> importarDesdeCSV(@RequestParam("archivo") MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío");
            }

            if (!archivo.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("El archivo debe ser de tipo CSV");
            }

            List<Hecho> hechosImportados = fuenteEstaticaService.importarDesdeArchivo(archivo);
            return ResponseEntity.ok("Se importaron " + hechosImportados.size() + " hechos exitosamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al importar archivo: " + e.getMessage());
        }
    }

    @PostMapping("/importar-csv-ruta")
    public ResponseEntity<String> importarDesdeRutaCSV(@RequestParam("ruta") String rutaArchivo) {
        try {
            List<Hecho> hechosImportados = fuenteEstaticaService.importarDesdeRuta(rutaArchivo);
            return ResponseEntity.ok("Se importaron " + hechosImportados.size() + " hechos desde " + rutaArchivo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al importar desde ruta: " + e.getMessage());
        }
    }

    @GetMapping("/sincronizar")
    public ResponseEntity<List<Hecho>> sincronizarFuenteEstatica() {
        try {
            List<Hecho> hechos = fuenteEstaticaService.sincronizar();
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estado")
    public ResponseEntity<String> obtenerEstado() {
        return ResponseEntity.ok("Fuente Estática operativa");
    }
}
