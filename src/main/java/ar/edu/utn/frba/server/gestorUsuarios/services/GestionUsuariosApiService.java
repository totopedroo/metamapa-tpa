package ar.edu.utn.frba.server.gestorUsuarios.services;

import ar.edu.utn.frba.client.dtos.AuthResponseDTO;
import ar.edu.utn.frba.client.dtos.RolesPermisosDTO;

/**
 * Servicio que habla con el backend de autenticación.
 */
public interface GestionUsuariosApiService {
    AuthResponseDTO login(String username, String password);
    RolesPermisosDTO getRolesPermisos(String accessToken);}