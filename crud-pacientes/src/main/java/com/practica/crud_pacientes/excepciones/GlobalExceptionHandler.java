package com.practica.crud_pacientes.excepciones;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PacienteDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handlerPacienteDuplicado(PacienteDuplicadoException exception, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                request.getContextPath()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PacienteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlerPacienteNoEncontrado(PacienteNoEncontradoException exception, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                request.getContextPath()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
