package ar.edu.utn.frba.Server.Servicio_Agregador.Service;

import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosInputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Server.Servicio_Agregador.Repository.HechoJpaRepository;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Service.NormalizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("hechosAgregadorService")
public class HechosService implements IHechosService {

        @Autowired
        private HechoJpaRepository hechosRepository;

        @Autowired
        private NormalizadorService normalizadorService;

        @Override
        public HechosOutputDto convertirDto(Hecho hecho) {
                HechosOutputDto hechosOutputDto = new HechosOutputDto(
                                hecho.getIdHecho(),
                                hecho.getTitulo(),
                                hecho.getDescripcion(),
                                hecho.getCategoria(),
                                hecho.getContenidoMultimedia(),
                                hecho.getLatitud(),
                                hecho.getLongitud(),
                                hecho.getFechaAcontecimiento(),
                                hecho.getFechaCarga(),
                                hecho.getEtiquetas(),
                                hecho.getSolicitudes(),
                                hecho.getContribuyente());
                return hechosOutputDto;

        }

        public List<HechosOutputDto> filtrarHechos(String categoria,
                        LocalDate fechaReporteDesde, LocalDate fechaReporteHasta,
                        LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta,
                        Double latitud, Double longitud) {
                List<Hecho> hechos = hechosRepository.findByEliminadoFalse();

                return hechos.stream()
                                .filter(h -> categoria == null || h.getCategoria().equalsIgnoreCase(categoria))
                                .filter(h -> fechaReporteDesde == null
                                                || !h.getFechaCarga().isBefore(fechaReporteDesde))
                                .filter(h -> fechaReporteHasta == null || !h.getFechaCarga().isAfter(fechaReporteHasta))
                                .filter(h -> fechaAcontecimientoDesde == null
                                                || !h.getFechaAcontecimiento().isBefore(fechaAcontecimientoDesde))
                                .filter(h -> fechaAcontecimientoHasta == null
                                                || !h.getFechaAcontecimiento().isAfter(fechaAcontecimientoHasta))
                                .filter(h -> latitud == null || Objects.equals(h.getLatitud(), latitud))
                                .filter(h -> longitud == null || Objects.equals(h.getLongitud(), longitud))
                                .map(h -> HechosOutputDto.fromModel(h)).collect(Collectors.toList());

        }

        public HechosOutputDto crearHecho(HechosInputDto inputDto) {
                // Normalizar datos de entrada
                String tituloNormalizado = normalizadorService.normalizarTitulo(inputDto.getTitulo());
                String descripcionNormalizada = normalizadorService.normalizarDescripcion(inputDto.getDescripcion());
                String categoriaNormalizada = normalizadorService.normalizarCategoria(inputDto.getCategoria());
                String provinciaNormalizada = normalizadorService.normalizarProvincia(inputDto.getProvincia());
                Double latitudNormalizada = normalizadorService.normalizarLatitud(inputDto.getLatitud());
                Double longitudNormalizada = normalizadorService.normalizarLongitud(inputDto.getLongitud());

                Hecho hecho = new Hecho();
                hecho.setTitulo(tituloNormalizado);
                hecho.setDescripcion(descripcionNormalizada);
                hecho.setCategoria(categoriaNormalizada);
                hecho.setContenidoMultimedia(inputDto.getContenidoMultimedia());
                hecho.setLatitud(latitudNormalizada);
                hecho.setLongitud(longitudNormalizada);
                hecho.setFechaAcontecimiento(inputDto.getFechaAcontecimiento());
                hecho.setHoraAcontecimiento(inputDto.getHoraAcontecimiento());
                hecho.setProvincia(provinciaNormalizada);
                hecho.setFechaCarga(LocalDate.now());
                hecho.setEliminado(false);
                hecho.setConsensuado(false);

                Hecho hechoGuardado = hechosRepository.save(hecho);
                return HechosOutputDto.fromModel(hechoGuardado);
        }

        public HechosOutputDto obtenerHecho(Long id) {
                Hecho hecho = hechosRepository.findById(id).orElse(null);
                if (hecho == null) {
                        return null;
                }
                return HechosOutputDto.fromModel(hecho);
        }

        public void setConsensuado(Hecho hecho) {
                Optional<Hecho> hechoExistente = hechosRepository.findById(hecho.getIdHecho());
                if (hechoExistente.isPresent()) {
                        Hecho h = hechoExistente.get();
                        h.setConsensuado(true);
                        hechosRepository.save(h);
                }
        }
}
