package ar.edu.utn.frba.Controllers;

import ar.edu.utn.frba.Dtos.HechosInputDto;
import ar.edu.utn.frba.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Service.Impl.FuenteDinamicaService;
import ar.edu.utn.frba.Service.ServicioAgregador;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agregador")
@CrossOrigin("http://localhost:8080")
public class ServicioAgregadorController {

    @Autowired
    private FuenteDinamicaService fuenteDinamicaService;

    @Autowired
    private ServicioAgregador servicioAgregador;

    @GetMapping("/hechos")
    public ResponseEntity<List<Hecho>> obtenerHechos() {
        return ResponseEntity.ok(servicioAgregador.agregarHechosDesdeTodasLasFuentes());
    }

    @GetMapping("/sincronizar")
    public String sincronizarHechos() {
        servicioAgregador.sincronizarConRepositorio();
        return "Hechos sincronizados con el repositorio en memoria.";
    }

   /* @GetMapping("/refrescar") // Para probar que anda el refresco automatico de colecciones.
    public ResponseEntity<String> refrescarManual() {
        servicioAgregador.refrescarHechosPeriodicamente();
        return ResponseEntity.ok("✔️ Refresco manual ejecutado correctamente");
    }*/

    @PostMapping("/crear")
    public ResponseEntity<HechosOutputDto> crearHecho(@RequestBody HechosInputDto hechoInputDto) {

        try {

            HechosOutputDto createdHecho = fuenteDinamicaService.crearHecho(hechoInputDto);
            return new ResponseEntity<>(createdHecho, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(new HechosOutputDto(null, "Error al crear el hecho: " + e.getMessage(), null, null, null, null, null, null, null, null, null, null));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HechosOutputDto(null, "Error interno del servidor: " + e.getMessage(), null, null, null, null, null, null, null, null, null, null));
        }
    }

    @PatchMapping("/editar/{hechoId}")
    public ResponseEntity<HechosOutputDto> editarHecho(@RequestBody HechosOutputDto hechosOutputDto,@PathVariable Long hechoId)
        {
            try {

                HechosOutputDto editedHecho = fuenteDinamicaService.editarHecho(hechosOutputDto.getIdHecho(), hechosOutputDto );
                return new ResponseEntity<>(editedHecho, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {

                return ResponseEntity.badRequest().body(new HechosOutputDto(null, "Error al crear el hecho: " + e.getMessage(), null, null, null, null, null, null, null, null, null, null));
            } catch (Exception e) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HechosOutputDto(null, "Error interno del servidor: " + e.getMessage(), null, null, null, null, null, null, null, null, null, null));
            }
        }
    }


