package ar.edu.utn.frba.Servicio_Agregador.Service;

import ar.edu.utn.frba.Servicio_Agregador.Dtos.ColeccionOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Enums.TipoFuente;
import ar.edu.utn.frba.Fuente_Estatica.Domain.ImportadorCSV;
import ar.edu.utn.frba.Servicio_Agregador.Repository.IColeccionRepository;
import ar.edu.utn.frba.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Servicio_Agregador.Service.IColeccionService;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Coleccion;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Fuente;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Servicio_Agregador.Domain.ImportadorAPI;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.CuradaStrategy;
import ar.edu.utn.frba.Servicio_Agregador.Service.ModoNavegacion.IrrestrictaStrategy;
import ar.edu.utn.frba.domain.main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ColeccionService implements IColeccionService {

    @Autowired
    private IColeccionRepository coleccionRepository;

    @Autowired
    private ImportadorAPI importadorAPI;

    @Autowired
    private ImportadorCSV importadorCSV;

    @Autowired
    private IHechosRepository hechosRepository;

    @Autowired
    private CuradaStrategy curadaStrategy;

    @Autowired
    private IrrestrictaStrategy irrestrictaStrategy;

    @Override
    public List<ColeccionOutputDto> buscarTodos() {
        List<Coleccion> colecciones = coleccionRepository.findAll();
        return colecciones.stream().map(this::coleccionOutputDto).toList();
    }

    public ColeccionOutputDto coleccionOutputDto(Coleccion coleccion) {
        var dto = new ColeccionOutputDto();
        dto.setId(coleccion.getId());
        dto.setHechos(coleccion.getHechos());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setCriterioDePertenencia(coleccion.getCriterioDePertenencia());
        return dto;
    }

    public List<HechosOutputDto> obtenerHechosPorColeccion(String idColeccion) {
        Coleccion coleccion = coleccionRepository.findById(idColeccion);
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + idColeccion);
        }
        return coleccion.getHechos().stream()
                .map(HechosOutputDto::fromModel)
                .collect(Collectors.toList());
    }

    public Coleccion setColeccionApi() {

        List<Hecho> hechosImportados;

        Fuente fuente = new Fuente("https://api-ddsi.disilab.ar/public/api/desastres", this.importadorAPI, TipoFuente.PROXY);
        hechosImportados = this.importadorAPI.importar(fuente);
        Coleccion coleccion = new Coleccion(
                UUID.randomUUID().toString(),
                "COLECCION API",
                "Colección creada a partir de datos de API",
                new ArrayList<>()
        );
        if (hechosImportados != null) {
            for (Hecho hecho : hechosImportados) {
                if (hecho != null) {
                    coleccion.setHecho(hecho);
                }
            }
        }
        coleccionRepository.save(coleccion);
        return coleccion;
    }


 /*public Coleccion setColeccionCsv() {
     List<Hecho> hechosImportadosCSV;
     Fuente fuente = new Fuente("src/main/java/ar/edu/utn/frba/Assets/prueba1.csv", this.importadorCSV, TipoFuente.LOCAL);
     hechosImportadosCSV = this.importadorCSV.importar(fuente);
     Coleccion coleccionCSV = new Coleccion(
             UUID.randomUUID().toString(),
             "COLECCION CSV",
             "Colección creada a partir de datos de CSV",
             new ArrayList<>()
     );
        coleccionCSV.setHechos(hechosImportadosCSV);
        coleccionRepository.save(coleccionCSV);
        return coleccionCSV;
    }
*/
    public void actualizarHechos(List<Hecho> nuevosHechos) {
        for (Coleccion coleccion : coleccionRepository.findAll()) {
            for (Hecho hecho : nuevosHechos) {
                if (hecho.estaEliminado()) continue;

                boolean cumple = coleccion.getCriterioDePertenencia()
                        .stream()
                        .allMatch(c -> c.cumple(hecho));

                if (cumple) {
                    coleccion.setHecho(hecho);
                }
            }
            coleccionRepository.save(coleccion);
        }
    }

    @Override
    public ColeccionOutputDto agregarHechoAColeccion(String coleccionId, Long hechoId) {
        Coleccion coleccion = coleccionRepository.findById(coleccionId); //
        if (coleccion == null) {
            throw new RuntimeException("Colección no encontrada con ID: " + coleccionId);
        }
        Hecho hecho = hechosRepository.findById(hechoId); // Assumes hechosRepository.findById exists and works
        if (hecho == null) {
            throw new RuntimeException("Hecho no encontrado con ID: " + hechoId);
        }
        if (hecho.estaEliminado()) { //
            throw new IllegalArgumentException("El hecho '" + hecho.getTitulo() + "' está marcado como eliminado y no puede ser agregado.");
        }
        coleccion.setHecho(hecho); //
        return coleccionOutputDto(coleccion); // Convert to DTO for the response
    }

    @Override
    public List<HechosOutputDto> navegarHechos(String coleccionId, String modo) {
        // 1. Obtener hechos ya en DTO
        List<HechosOutputDto> hechosDTOs = this.obtenerHechosPorColeccion(coleccionId);

        // 2. Mapear a entidad Hecho (usamos setters porque no hay constructor completo)
        List<Hecho> hechos = hechosDTOs.stream().map(dto -> {
            Hecho h = new Hecho(
                    dto.getTitulo(),
                    dto.getDescripcion(),
                    dto.getCategoria(),
                    dto.getContenidoMultimedia().orElse(null),
                    dto.getLatitud(),
                    dto.getLongitud(),
                    dto.getFechaAcontecimiento(),
                    dto.getFechaCarga(),
                    dto.getIdHecho()
            );
            h.setEtiquetas(dto.getEtiquetas());
            h.setSolicitudes(dto.getSolicitudes());
            h.setContribuyente(dto.getContribuyente());
            h.setEliminado(dto.isEliminado());
            h.setConsensuado(Optional.of(dto.isConsensuado()));
            h.setFuente(Optional.ofNullable(dto.getFuente()));
            return h;
        }).toList();

        // 3. Aplicar estrategia
        List<Hecho> hechosFiltrados = "curada".equalsIgnoreCase(modo)
                ? curadaStrategy.filtrar(hechos)
                : irrestrictaStrategy.filtrar(hechos);

        // 4. Volver a mapear a DTO usando tu metodo existente
        return hechosFiltrados.stream()
                .map(HechosOutputDto::fromModel)
                .toList();
    }

}
