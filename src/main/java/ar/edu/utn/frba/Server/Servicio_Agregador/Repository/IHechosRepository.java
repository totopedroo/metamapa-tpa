package ar.edu.utn.frba.Server.Servicio_Agregador.Repository;

import ar.edu.utn.frba.Server.Servicio_Agregador.Domain.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IHechosRepository extends JpaRepository <Hecho, Long> {
/*
    public List<Hecho> findAll();
    public Hecho save(Hecho hecho);
    public void delete(Hecho hecho);
    public Hecho findById(long id);
    public void saveList(List<Hecho> listaHechos);
    public Hecho create(Hecho hecho);

 */
}