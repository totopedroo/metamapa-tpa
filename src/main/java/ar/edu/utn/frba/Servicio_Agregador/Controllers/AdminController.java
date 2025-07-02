package ar.edu.utn.frba.Servicio_Agregador.Controllers;

import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionInputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Service.IColeccionService;
import ar.edu.utn.frba.Servicio_Agregador.Service.ISolicitudService;
import ar.edu.utn.frba.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Repository.IColeccionRepository;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Coleccion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API administrativa de MetaMapa para operaciones CRUD sobre colecciones
 * y gestión de solicitudes de eliminación
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:8080")
public class AdminController {

    @Autowired
    @Qualifier("coleccionService")
    private IColeccionService coleccionService;

    @Autowired
    @Qualifier("solicitudAgregadorService")
    private ISolicitudService solicitudService;

    @Autowired
    @Qualifier("coleccionRepository")
    private IColeccionRepository coleccionRepository;

    /**
     * GET /admin/colecciones
     * Obtiene todas las colecciones
     */
    @GetMapping("/colecciones")
    public ResponseEntity<List<ColeccionOutputDto>> obtenerTodasLasColecciones() {
        try {
            List<ColeccionOutputDto> colecciones = coleccionService.buscarTodos();
            return ResponseEntity.ok(colecciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /admin/colecciones/{id}
     * Obtiene una colección específica por ID
     */
    @GetMapping("/colecciones/{id}")
    public ResponseEntity<ColeccionOutputDto> obtenerColeccionPorId(@PathVariable String id) {
        try {
            Coleccion coleccion = coleccionRepository.findById(id);
            if (coleccion == null) {
                return ResponseEntity.notFound().build();
            }
            ColeccionOutputDto dto = new ColeccionOutputDto();
            dto.setId(coleccion.getId());
            dto.setTitulo(coleccion.getTitulo());
            dto.setDescripcion(coleccion.getDescripcion());
            dto.setHechos(coleccion.getHechos());
            dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /admin/colecciones
     * Crea una nueva colección
     */
    @PostMapping("/colecciones")
    public ResponseEntity<ColeccionOutputDto> crearColeccion(@RequestBody ColeccionInputDto coleccionInput) {
        try {
            Coleccion nuevaColeccion = coleccionRepository.create(coleccionInput);
            ColeccionOutputDto dto = new ColeccionOutputDto();
            dto.setId(nuevaColeccion.getId());
            dto.setTitulo(nuevaColeccion.getTitulo());
            dto.setDescripcion(nuevaColeccion.getDescripcion());
            dto.setHechos(nuevaColeccion.getHechos());
            dto.setCriterioDePertenencia(nuevaColeccion.getCriterioDePertenencia());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /admin/colecciones/{id}
     * Actualiza una colección existente
     */
    @PutMapping("/colecciones/{id}")
    public ResponseEntity<ColeccionOutputDto> actualizarColeccion(
            @PathVariable String id,
            @RequestBody ColeccionInputDto coleccionInput) {
        try {
            Coleccion coleccion = coleccionRepository.findById(id);
            if (coleccion == null) {
                return ResponseEntity.notFound().build();
            }

            // Actualizar campos de la colección
            coleccion.setTitulo(coleccionInput.getTitulo());
            coleccion.setDescripcion(coleccionInput.getDescripcion());
            if (coleccionInput.getCriterioDePertenencia() != null) {
                coleccion.setCriterioDePertenencia(coleccionInput.getCriterioDePertenencia());
            }

            coleccionRepository.save(coleccion);
            ColeccionOutputDto dto = new ColeccionOutputDto();
            dto.setId(coleccion.getId());
            dto.setTitulo(coleccion.getTitulo());
            dto.setDescripcion(coleccion.getDescripcion());
            dto.setHechos(coleccion.getHechos());
            dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /admin/colecciones/{id}
     * Elimina una colección
     */
    @DeleteMapping("/colecciones/{id}")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable String id) {
        try {
            Coleccion coleccion = coleccionRepository.findById(id);
            if (coleccion == null) {
                return ResponseEntity.notFound().build();
            }
            coleccionRepository.delete(coleccion);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /admin/colecciones/{id}/hechos
     * Obtiene todos los hechos de una colección específica
     */
    @GetMapping("/colecciones/{id}/hechos")
    public ResponseEntity<List<HechosOutputDto>> obtenerHechosDeColeccion(@PathVariable String id) {
        try {
            List<HechosOutputDto> hechos = coleccionService.obtenerHechosPorColeccion(id);
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /admin/colecciones/{coleccionId}/hechos/{hechoId}
     * Agrega un hecho a una colección
     */
    @PostMapping("/colecciones/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<ColeccionOutputDto> agregarHechoAColeccion(
            @PathVariable String coleccionId,
            @PathVariable Long hechoId) {
        try {
            ColeccionOutputDto coleccion = coleccionService.agregarHechoAColeccion(coleccionId, hechoId);
            return ResponseEntity.ok(coleccion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /admin/colecciones/{coleccionId}/hechos/{hechoId}
     * Remueve un hecho de una colección
     */
    @DeleteMapping("/colecciones/{coleccionId}/hechos/{hechoId}")
    public ResponseEntity<Void> removerHechoDeColeccion(
            @PathVariable String coleccionId,
            @PathVariable Long hechoId) {
        try {
            Coleccion coleccion = coleccionRepository.findById(coleccionId);
            if (coleccion == null) {
                return ResponseEntity.notFound().build();
            }

            // Remover el hecho de la colección
            coleccion.getHechos().removeIf(hecho -> hecho.getIdHecho().equals(hechoId));
            coleccionRepository.save(coleccion);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /admin/colecciones/{id}/algoritmo-consenso
     * Modifica el algoritmo de consenso de una colección
     */
    @PutMapping("/colecciones/{id}/algoritmo-consenso")
    public ResponseEntity<ColeccionOutputDto> modificarAlgoritmoConsenso(
            @PathVariable String id,
            @RequestBody String algoritmo) {
        try {
            Coleccion coleccion = coleccionRepository.findById(id);
            if (coleccion == null) {
                return ResponseEntity.notFound().build();
            }

            // Aquí podrías implementar la lógica para cambiar el algoritmo de consenso
            // Por ahora, simplemente actualizamos la colección
            coleccionRepository.save(coleccion);

            ColeccionOutputDto dto = new ColeccionOutputDto();
            dto.setId(coleccion.getId());
            dto.setTitulo(coleccion.getTitulo());
            dto.setDescripcion(coleccion.getDescripcion());
            dto.setHechos(coleccion.getHechos());
            dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /admin/solicitudes/{id}/aprobar
     * Aprueba una solicitud de eliminación
     */
    @PostMapping("/solicitudes/{id}/aprobar")
    public ResponseEntity<Void> aprobarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.aceptarSolicitud(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /admin/solicitudes/{id}/denegar
     * Deniega una solicitud de eliminación
     */
    @PostMapping("/solicitudes/{id}/denegar")
    public ResponseEntity<Void> denegarSolicitud(@PathVariable Long id) {
        try {
            solicitudService.rechazarSolicitud(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /admin/estado
     * Obtiene el estado del sistema administrativo
     */
    @GetMapping("/estado")
    public ResponseEntity<String> obtenerEstadoAdmin() {
        return ResponseEntity.ok("Sistema administrativo MetaMapa operativo");
    }
}
