package ar.edu.utn.frba.server.fuente.estatica.controllers;

import ar.edu.utn.frba.server.fuente.estatica.services.IFuenteEstaticaService;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fuente-estatica")
@CrossOrigin("http://localhost:8080")
@RequiredArgsConstructor
public class FuenteEstaticaController {



    @Autowired
    private IFuenteEstaticaService fuenteEstaticaService;

    @GetMapping("/hechos")
    @Transactional
    public ResponseEntity<List<Hecho>> obtenerHechos() {
        List<Hecho> hechos = fuenteEstaticaService.obtenerTodosHechos();
        return ResponseEntity.ok(hechos);
    }
    @PostMapping("/importar-csv-ruta")
    public ResponseEntity<String> importarDesdeRutaCSV(@RequestParam("ruta") String rutaArchivo) {
        try {
            List<Hecho> hechosImportados = fuenteEstaticaService.importarHechos(rutaArchivo);
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

    @PostMapping(
            value = "/importar-csv",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importarDesdeUpload(@RequestParam("file") MultipartFile file) {
        try {
            List<Hecho> hechos = fuenteEstaticaService.importarDesdeArchivo(file);
            return ResponseEntity.ok("Se importaron " + hechos.size() + " hechos.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al importar CSV: " + e.getMessage());
        }
    }

    @GetMapping("/estado")
    public ResponseEntity<String> obtenerEstado() {
        return ResponseEntity.ok("Fuente Estática operativa");
    }
}
