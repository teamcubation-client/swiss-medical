package com.teamcubation.api.pacientes.shared.exception;

import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.ApiResponse;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String GENERIC_ERROR = "Ocurrió un error inesperado: ";
    private static final String INVALID_REQUEST_DATA_ERROR = "Datos ingresados no válidos: ";

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiResponse<String>> handleUnsupportedOperation(UnsupportedOperationException ex) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ErrorResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handlePatientNotFound(PatientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(DuplicatedPatientException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicatedPatient(DuplicatedPatientException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(PatientDniAlreadyInUseException.class)
    public ResponseEntity<ApiResponse<String>> PatientDniAlreadyInUse(PatientDniAlreadyInUseException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(ExporterTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleExporterTypeNotSupportedException(ExporterTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse<>(ex.getMessage()));

    }

    @ExceptionHandler(JsonExportException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonExportException(JsonExportException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(INVALID_REQUEST_DATA_ERROR + ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(INVALID_REQUEST_DATA_ERROR + ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse<>(INVALID_REQUEST_DATA_ERROR + ex.getMessage()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse<>(GENERIC_ERROR + ex.getMessage()));
    }

}