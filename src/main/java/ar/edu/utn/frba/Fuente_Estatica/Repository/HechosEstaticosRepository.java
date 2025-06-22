package ar.edu.utn.frba.Fuente_Estatica.Repository;

import ar.edu.utn.frba.Fuente_Estatica.Domain.HechoEstatico;
import ar.edu.utn.frba.domain.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository

public class HechosEstaticosRepository implements IHechosEstaticosRepository {

    private List<HechoEstatico> hechos;

    public HechosEstaticosRepository() {
        hechos = new ArrayList<HechoEstatico>();
    }
    @Override
    public void save(HechoEstatico hecho) {
        hechos.add(hecho);
    }

    @Override
    public List<HechoEstatico> findAll() {
        return hechos;
    }

    @Override
    public List<HechoEstatico> buscarNoSicronizados() {
        return hechos.stream().filter(h-> h.getEstaSincronizado()==false).toList();
    }
}
