package ar.edu.utn.frba.Fuente_Estatica.Service;

import ar.edu.utn.frba.domain.Hecho;

import java.util.List;

public interface IFuenteEstaticaService {
    public List<Hecho> sincronizar();
}
