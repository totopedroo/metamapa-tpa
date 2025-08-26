package ar.edu.utn.frba.Server.Fuente_Dinamica.Controllers;

import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosInputDto;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Fuente_Dinamica.Service.IFuenteDinamicaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fuente-dinamica")
@CrossOrigin("http://localhost:8080")
public class FuenteDinamicaController {

    @Autowired
    private IFuenteDinamicaService fuenteDinamicaService;

    @PostMapping("/hechos")
    public ResponseEntity<HechosOutputDto> crearHecho(@RequestBody HechosInputDto hechoInputDto) {
        try {
            HechosOutputDto createdHecho = fuenteDinamicaService.crearHecho(hechoInputDto);
            return new ResponseEntity<>(createdHecho, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new HechosOutputDto(null, "Error al crear el hecho: " + e.getMessage(),
                            null, null, null, null, null, null, null, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HechosOutputDto(null, "Error interno del servidor: " + e.getMessage(),
                            null, null, null, null, null, null, null, null, null, null));
        }
    }

    @PatchMapping("/hechos/{hechoId}")
    public ResponseEntity<HechosOutputDto> editarHecho(@RequestBody HechosOutputDto hechosOutputDto,
            @PathVariable Long hechoId) {
        try {
            HechosOutputDto editedHecho = fuenteDinamicaService.editarHecho(hechoId, hechosOutputDto);
            return ResponseEntity.ok(editedHecho);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new HechosOutputDto(null, "Error al editar el hecho: " + e.getMessage(),
                            null, null, null, null, null, null, null, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new HechosOutputDto(null, "Error interno del servidor: " + e.getMessage(),
                            null, null, null, null, null, null, null, null, null, null));
        }
    }

    @GetMapping("/estado")
    public ResponseEntity<String> obtenerEstado() {
        return ResponseEntity.ok("Fuente Dinámica operativa");
    }
}
