package com.teamcubation.api.pacientes.shared.exception;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.ApiResponse;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_GENERICO = "Ocurrió un error inesperado: ";

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiResponse<String>> handleUnsupportedOperation(UnsupportedOperationException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handlePacienteNotFound(PatientNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedPatientException.class)
    public ResponseEntity<ApiResponse<String>> handlePacienteDuplicado(DuplicatedPatientException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PatientDniAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<String>> handlePacienteNoActualizado(PatientDniAlreadyInUseException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ExporterTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleExporterTypeNotSupportedException(ExporterTypeNotSupportedException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonExportException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonExportException(JsonExportException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleErrorGenerico(Exception ex) {
        return buildErrorResponse(ERROR_GENERICO + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<String>> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorResponse<>(message));
    }
}