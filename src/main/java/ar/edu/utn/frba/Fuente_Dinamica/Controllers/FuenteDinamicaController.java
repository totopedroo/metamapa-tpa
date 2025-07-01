package ar.edu.utn.frba.Fuente_Dinamica.Controllers;
import ar.edu.utn.frba.Fuente_Dinamica.Service.FuenteDinamicaService;
import ar.edu.utn.frba.Fuente_Dinamica.Dtos.HechosInputDto;
import ar.edu.utn.frba.Fuente_Dinamica.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Fuente_Dinamica.Service.IFuenteDinamicaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FuenteDinamicaController {
    @Autowired
    private IFuenteDinamicaService fuenteDinamicaService;
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
    public ResponseEntity<ar.edu.utn.frba.Fuente_Dinamica.Dtos.HechosOutputDto> editarHecho(@RequestBody HechosOutputDto hechosOutputDto, @PathVariable Long hechoId)
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


