package com.practica.crud_pacientes.shared.exceptions;

import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final PacienteLoggerPort loggerPort;

    @ExceptionHandler(PacienteDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlePacienteDuplicado(PacienteDuplicadoException exception, HttpServletRequest request){
        loggerPort.warn("Paciente duplicado: {} - Path: {}", exception.getMessage(), request.getRequestURI());
        return buildError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(PacienteNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePacienteNoEncontrado(PacienteNoEncontradoException exception, HttpServletRequest request){
        loggerPort.warn("Paciente no encontrado: {} - Path: {}", exception.getMessage(), request.getRequestURI());
        return buildError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationsException(MethodArgumentNotValidException exception, HttpServletRequest request){
        Map<String, String> errores = new HashMap<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        loggerPort.warn("Errores de validación en {}: {}", request.getRequestURI(), errores);
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Errores de validacion",
                request.getRequestURI(),
                errores
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExcepcionGenerica(Exception ex, HttpServletRequest request) {
        String mensaje = "Ocurrió un error inesperado: " + ex.getMessage();
        loggerPort.error("Error inesperado en {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                mensaje,
                request.getRequestURI()
        );
    }

    private ErrorResponse buildError(HttpStatus status, String message, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                status.getReasonPhrase(),
                path
        );
    }

    private ErrorResponse buildError(HttpStatus status, String message, String path, Map<String, String> errores) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                status.getReasonPhrase(),
                path,
                errores
        );
    }
}
