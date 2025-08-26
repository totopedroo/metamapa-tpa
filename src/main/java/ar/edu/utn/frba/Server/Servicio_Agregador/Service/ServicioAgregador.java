package ar.edu.utn.frba.Server.Servicio_Agregador.Service;

import ar.edu.utn.frba.Server.Enums.TipoFuente;
import ar.edu.utn.frba.Server.Fuente_Estatica.Domain.ImportadorCSV;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.ImportadorAPI;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.ImportadorMetaMapa;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso.AlgoritmoDeConsensoStrategy;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.Consenso.ConsensoPorDefectoStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioAgregador {

  private final List<Fuente> fuentes;
  private final IHechosRepository hechosRepository;
  @Autowired
  private ColeccionService coleccionService;

  public ServicioAgregador(ImportadorCSV importadorCSV, ImportadorAPI importadorAPI,
      ImportadorMetaMapa importadorMetaMapa, IHechosRepository hechosRepository) {
    this.hechosRepository = hechosRepository;
    this.fuentes = List.of(
    // new Fuente("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv", (Importador)
    // importadorCSV, TipoFuente.LOCAL),
    //new Fuente("https://api-ddsi.disilab.ar/public/api/desastres", (Importador)
    //importadorAPI, TipoFuente.PROXY) // Comentado temporalmente por
    //incompatibilidad de tipos
    // new Fuente("http://localhost:8080", (Importador) importadorMetaMapa,
    // TipoFuente.PROXY) falta tener URL de otra instancia MetaMapa.
    );
  }

  @PostConstruct
  public List<Hecho> agregarHechosDesdeTodasLasFuentes() {
    List<Hecho> hechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      hechos.addAll(fuente.obtenerHechos());
    }
    for (Hecho hecho : hechos) {
      hechosRepository.save(hecho);
    }
    return hechos;
  }

  /*
   * public void sincronizarConRepositorio() {
   * List<Hecho> hechos = agregarHechosDesdeTodasLasFuentes();
   * //System.out.println("HECHOS OBTENIDOS DE FUENTES: " + hechos);
   * for (Hecho hecho : hechos) {
   * hechosRepository.save(hecho);
   * }
   * }
   */
  @Scheduled(fixedRate = 3600000) // cada 1 hora (en milisegundos)
  public void refrescarHechosPeriodicamente() {
    System.out.println("⏱️ Iniciando refresco automático de colecciones...");

    List<Hecho> nuevosHechos = new ArrayList<>();
    for (Fuente fuente : fuentes) {
      if (fuente.getTipo() != TipoFuente.PROXY) {
        nuevosHechos.addAll(fuente.obtenerHechos());
      }
    }

    coleccionService.actualizarHechos(nuevosHechos);

    System.out.println("Refresco automático finalizado.");

    System.out.println(" Refresco automático finalizado.");

  }

  @Scheduled(cron = "0 0 3 * * *")
  public void ejecutarAlgoritmosDeConsenso() {
      System.out.println("Ejecutando algoritmos de consenso...");

      List<Hecho> todosLosHechos = coleccionService.buscarTodos().stream()
              .flatMap(coleccion -> coleccion.getHechos().stream())
              .toList();

      coleccionService.buscarTodos().forEach(coleccion -> {

        AlgoritmoDeConsensoStrategy algoritmo = coleccion.getAlgoritmoDeConsenso();
        String nombreColeccion = coleccion.getTitulo();


        if (algoritmo == null) {
          algoritmo = new ConsensoPorDefectoStrategy();
          System.out.println("  - Colección '" + nombreColeccion + "': No tiene algoritmo. Usando estrategia por defecto.");
        } else {
          System.out.println("  - Colección '" + nombreColeccion + "': Aplicando algoritmo '" + algoritmo.getClass().getSimpleName() + "'.");
        }
        final AlgoritmoDeConsensoStrategy algoritmoFinal = algoritmo;
        coleccion.getHechos().forEach(hecho ->
                algoritmoFinal.procesarYEstablecerConsenso(hecho, todosLosHechos)
        );
      });

      System.out.println("Algoritmos de consenso finalizados.");
    }
  }
