package com.tq.pacientes.shared.exceptions;

import com.tq.pacientes.infrastructure.adapter.in.rest.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private <T extends Exception> ResponseEntity<ErrorDTO> buildResponse(T ex, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ErrorDTO(status.value(), ex.getMessage()));
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(PatientNotFoundException ex) {
        return buildResponse(ex, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(DuplicatePatientException.class)
    public ResponseEntity<ErrorDTO> handleDuplicate(DuplicatePatientException ex) {
        return buildResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PatientDniNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleDniNotFound(PatientDniNotFoundException ex) {
        return buildResponse(ex, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(PatientAlreadyActiveException.class)
    public ResponseEntity<ErrorDTO> handleAlreadyActive(PatientAlreadyActiveException ex) {
        return buildResponse(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingBirthDateException.class)
    public ResponseEntity<ErrorDTO> handleMissingBirthDate(MissingBirthDateException ex) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPatientAgeException.class)
    public ResponseEntity<ErrorDTO> handleInvalidPatientAge(InvalidPatientAgeException ex) {
        return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unexpected error occurred: " + ex.getMessage()
                ));
    }
} 