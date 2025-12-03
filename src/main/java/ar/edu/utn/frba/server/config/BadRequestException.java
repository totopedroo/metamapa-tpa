package ar.edu.utn.frba.server.config;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}

