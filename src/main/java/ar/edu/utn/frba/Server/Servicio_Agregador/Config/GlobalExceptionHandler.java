package ar.edu.utn.frba.Server.Servicio_Agregador.Config;// package ar.edu.utn.frba.Server.Servicio_Agregador.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return ResponseEntity.internalServerError()
                .body(e.getClass().getName() + ": " + e.getMessage() + "\n\n" + sw);
    }
}