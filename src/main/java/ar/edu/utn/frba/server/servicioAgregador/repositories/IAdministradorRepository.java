package ar.edu.utn.frba.server.servicioAgregador.repositories;

import ar.edu.utn.frba.server.servicioAgregador.domain.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdministradorRepository extends JpaRepository<Administrador, Long> {}