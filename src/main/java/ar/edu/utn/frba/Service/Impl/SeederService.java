package ar.edu.utn.frba.Service.Impl;

import ar.edu.utn.frba.Repository.IHechosRepository;
import ar.edu.utn.frba.domain.Hecho;
import ar.edu.utn.frba.domain.Importador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ar.edu.utn.frba.Service.ISeederService;

import java.util.ArrayList;
import java.util.List;


@Service
public class SeederService implements ISeederService {
    @Autowired
    private IHechosRepository hechosRepository;
    //@Value("${app.base-url}")
    //private String baseurl;

    public void inicializar() {
        /*Importador importador = new Importador();
        importador.importarFromCSV("prueba1.csv");
        List<Hecho> hechos = new ArrayList<Hecho>();
        hechos = importador.getHechos();

        hechos.stream().forEach(h->hechosRepository.guardarHecho(h));*/

    }

}
