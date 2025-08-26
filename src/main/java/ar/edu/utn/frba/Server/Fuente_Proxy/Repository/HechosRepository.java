package ar.edu.utn.frba.Server.Fuente_Proxy.Repository;

import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.Fuente;
import ar.edu.utn.frba.Server.Fuente_Proxy.Domain.Hecho;
import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.ImportadorAPI;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("hechosProxyRepository")
public class HechosRepository implements IHechosRepository {

    public List<Hecho> hechosApi;
    public Fuente fuente;
    public ImportadorAPI importador;
    public List<Hecho> hechos;

    public HechosRepository() {
        this.hechos = new ArrayList<>();
    }

    /*
     * @Override
     * public List<Hecho> findAll() {
     * return new ArrayList<>(hechos);
     * }
     */

    @Override
    public Hecho findById(long id) {
        return this.hechos.stream().filter(g -> g.getIdHecho() == id).findFirst().orElse(null);
    }

    @Override

    public List<Hecho> findAll() {
        System.out.println("hechos: " + hechos);
        return hechos;
    }

    public Hecho create(Hecho hecho) {
        if (hecho == null) {
            throw new IllegalArgumentException("El hecho no puede ser nulo.");
        }
        this.hechos.add(hecho);
        System.out.println("Hecho creado y agregado a la lista: " + hecho.getTitulo());
        return hecho;
    }

    @Override
    public void save(Hecho hecho) {
        hechos.add(hecho);
    }

    public void saveList(List<Hecho> listaHechos) {
        hechos.addAll(listaHechos);
    }

    @Override
    public void delete(Hecho hecho) {
        hechos.remove(hecho);
    }
}
