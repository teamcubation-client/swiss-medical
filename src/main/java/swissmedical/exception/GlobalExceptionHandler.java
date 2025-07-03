package swissmedical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones
 * Proporciona respuestas HTTP para las excepciones de los pacientes
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Maneja la excepcion cuando no se encuentra un paciente
     * @param ex excepcion PacienteNotFoundException
     * @return respuesta HTTP 404 con el mensaje de error
     */
    @ExceptionHandler(PacienteNotFoundException.class)
    public ResponseEntity<String> handlePacienteNotFound(PacienteNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepcion cuando se detecta un paciente duplicado
     * @param ex excepcion PacienteDuplicadoException
     * @return respuesta HTTP 400 con el mensaje de error
     */
    @ExceptionHandler(PacienteDuplicadoException.class)
    public ResponseEntity<String> handlePacienteDuplicado(PacienteDuplicadoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
} 