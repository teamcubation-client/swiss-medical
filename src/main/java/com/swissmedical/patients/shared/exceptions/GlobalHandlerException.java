package com.swissmedical.patients.shared.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

  final String ERROR_KEY = "error";

  @ExceptionHandler(PatientNotFoundException.class)
  public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException ex) {
    Map<String, String> errorResponse = getErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler(PatientDuplicateException.class)
  public ResponseEntity<Map<String, String>> handlePatientDuplicateException(PatientDuplicateException ex) {
    Map<String, String> errorResponse = getErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = getErrorResponse(ex.getBindingResult().getFieldErrors());
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
    Map<String, String> errorResponse = getErrorResponse("Invalid argument: " + ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
    Map<String, String> errorResponse = getErrorResponse("An unexpected error occurred: " + ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public Map<String, String> getErrorResponse(String message) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put(ERROR_KEY, message);
    return errorResponse;
  }

  public Map<String, String> getErrorResponse(List<FieldError> errors) {
    Map<String, String> errorResponse = new HashMap<>();
    for (FieldError error : errors) {
      errorResponse.put(error.getField(), error.getDefaultMessage());
    }
    return errorResponse;
  }

}
