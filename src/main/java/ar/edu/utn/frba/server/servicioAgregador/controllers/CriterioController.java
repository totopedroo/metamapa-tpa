package ar.edu.utn.frba.server.servicioAgregador.controllers;

import ar.edu.utn.frba.server.servicioAgregador.domain.CriterioDePertenencia;
import ar.edu.utn.frba.server.servicioAgregador.dtos.CriterioDePertenenciaDto;
import ar.edu.utn.frba.server.servicioAgregador.repositories.ICriterioRepository;
import ar.edu.utn.frba.server.servicioAgregador.services.CriterioDePertenenciaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criterios")
@RequiredArgsConstructor
public class CriterioController {

    private final ICriterioRepository criterioRepo;

    @PostMapping
    public ResponseEntity<Long> crear(@RequestBody CriterioDePertenenciaDto dto) {

        CriterioDePertenencia criterio =
                CriterioDePertenenciaMapper.toDomain(dto);

        criterioRepo.save(criterio);

        return ResponseEntity.ok(criterio.getId_criterio());
    }

    @GetMapping
    public List<CriterioDePertenencia> listar() {
        return criterioRepo.findAll();
    }
}