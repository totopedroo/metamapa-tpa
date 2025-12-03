package ar.edu.utn.frba.server.gestorUsuarios.Exceptions;

public class UsuarioYaExistenteException extends RuntimeException {
    public UsuarioYaExistenteException(String message) {
        super(message);
    }

}
