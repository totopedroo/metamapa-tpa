package ar.edu.utn.frba.Servicio_Agregador.Service;

import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosInputDto;
import ar.edu.utn.frba.Servicio_Agregador.Dtos.HechosOutputDto;
import ar.edu.utn.frba.Servicio_Agregador.Repository.IHechosRepository;
import ar.edu.utn.frba.Servicio_Agregador.Service.IHechosService;
import ar.edu.utn.frba.Servicio_Agregador.Domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("hechosAgregadorService")
public class HechosService implements IHechosService {

        @Autowired
        @Qualifier("hechosAgregadorRepository")
        private IHechosRepository hechosRepository;

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
                List<Hecho> hechos = hechosRepository.findAll();

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
                                .map(h -> convertirDto(h)).collect(Collectors.toList());

        }

        public HechosOutputDto crearHecho(HechosInputDto inputDto) {
                Hecho hecho = new Hecho(
                                inputDto.getTitulo(),
                                inputDto.getDescripcion(),
                                inputDto.getCategoria(),
                                inputDto.getContenidoMultimedia().orElse(null),
                                inputDto.getLatitud(),
                                inputDto.getLongitud(),
                                inputDto.getFechaAcontecimiento(),
                                LocalDate.now(),
                                System.currentTimeMillis(),
                                inputDto.getFuente());

                hechosRepository.save(hecho);

                return new HechosOutputDto(
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
        }

        public HechosOutputDto obtenerHecho(Long id) {
                Hecho hecho = hechosRepository.findById(id);
                if (hecho == null) {
                        return null;
                }

                return new HechosOutputDto(
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
        }


       public void  setConsensuado(Hecho hecho) {
                hechosRepository.findById(hecho.getIdHecho());
                setConsensuado(hecho);
                hechosRepository.save(hecho);
       }
}
