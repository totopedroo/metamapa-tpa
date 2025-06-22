package ar.edu.utn.frba.Fuente_Estatica;

import ar.edu.utn.frba.Fuente_Estatica.Domain.HechoEstatico;
import ar.edu.utn.frba.Fuente_Estatica.Service.FuenteEstaticaService;
import ar.edu.utn.frba.Fuente_Estatica.Service.IFuenteEstaticaService;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping
@CrossOrigin("http://localhost:8080")
public class FuenteEstaticaController{
    private IFuenteEstaticaService fuenteEstaticaService;

    public FuenteEstaticaController() {
        this.fuenteEstaticaService = new FuenteEstaticaService();
    }

    @GetMapping("/fuenteEstatica")
    public List<Hecho> sincronizarFuenteEstatica(){
            return fuenteEstaticaService.sincronizar();
    }


}
