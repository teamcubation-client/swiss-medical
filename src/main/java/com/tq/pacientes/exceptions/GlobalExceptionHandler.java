package com.tq.pacientes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PacienteNoEncontradoException.class)
    public ResponseEntity<String> handleNoEncontrado(PacienteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PacienteDniNoEncontradoException.class)
    public ResponseEntity<String> handleDniNoEncontrado(PacienteDniNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PacienteDuplicadoException.class)
    public ResponseEntity<String> handleDuplicado(PacienteDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
