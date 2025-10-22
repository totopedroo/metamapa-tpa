package ar.edu.utn.frba.server.servicioUsuarios.exceptions;

public class UsuarioYaExistenteException extends RuntimeException {
    public UsuarioYaExistenteException(String message) {
        super(message);
    }

}
