package ar.edu.utn.frba.server.servicioAgregador.services;

import ar.edu.utn.frba.server.contratos.enums.TipoFuente;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.server.servicioAgregador.domain.consenso.ConsensoPorDefectoStrategy;
import ar.edu.utn.frba.server.servicioAgregador.repositories.IHechosRepository;
import ar.edu.utn.frba.server.servicioAgregador.domain.Hecho;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioAgregador {

  private static final String ORIGEN_METAMAPA = "proxy-metamapa";

  private final ConsultarHechosHandler handler;
  private final AgregadorMapper mapper;
  private final IHechosRepository hechosRepository;
  private final ColeccionService coleccionService;

  public ServicioAgregador(ConsultarHechosHandler handler,
                           AgregadorMapper mapper,
                           IHechosRepository hechosRepository,
                           ColeccionService coleccionService) {
    this.handler = handler;
    this.mapper = mapper;
    this.hechosRepository = hechosRepository;
    this.coleccionService = coleccionService;
  }

  @PostConstruct
  public List<Hecho> agregarHechosDesdeTodasLasFuentes() {
    var dtos = handler.consultar(null);
    var hechos = dtos.stream().map(mapper::toDomain).toList();
    hechos.forEach(hechosRepository::save);
    return hechos;
  }

  @Scheduled(fixedRate = 3_600_000) // cada 1 hora
  public void refrescarHechosPeriodicamente() {
    var dtosNoTiempoReal = handler.consultar(null).stream()
            .filter(d -> !ORIGEN_METAMAPA.equalsIgnoreCase(d.fuente()))
            .toList();

    var nuevosHechos = dtosNoTiempoReal.stream()
            .map(mapper::toDomain)
            .toList();

    coleccionService.actualizarHechos(nuevosHechos);
  }

  @Scheduled(cron = "0 0 3 * * *") // 03:00
  public void ejecutarAlgoritmosDeConsenso() {
    System.out.println("Ejecutando algoritmos de consenso...");

    // tomar todas las colecciones con el metodo correcto
    var colecciones = coleccionService.findAll();

    // iterar con for clásico (evitamos lambdas y el "effectively final")
    for (var coleccion : colecciones) {
      // snapshot tipado de hechos por colección (evita raw types)
      List<Hecho> universo = new ArrayList<>(
              coleccion.getHechos() == null ? List.of() : coleccion.getHechos()
      );

      // resolver algoritmo (guardo la Strategy; si es null, defecto)
      AlgoritmoDeConsensoStrategy algoritmo = coleccion.getAlgoritmoDeConsenso();
      if (algoritmo == null) {
        algoritmo = new ConsensoPorDefectoStrategy();
        System.out.println("  - Colección '" + coleccion.getTitulo() + "': sin algoritmo -> DEFECTO");
      } else {
        System.out.println("  - Colección '" + coleccion.getTitulo() + "': " + algoritmo.codigo());
      }

      // calcular y setear estado de consenso por hecho
      for (Hecho h : universo) {
        var res = algoritmo.evaluar(h, universo);   // devuelve ConsensoResult
        h.setEstadoConsenso(res.getEstado());       // flag listo para navegación "curada"
      }
    }

    System.out.println("Algoritmos de consenso finalizados.");
  }


}
