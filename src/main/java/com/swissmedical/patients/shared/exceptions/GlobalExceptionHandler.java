package com.swissmedical.patients.shared.exceptions;

import com.swissmedical.patients.infrastructure.adapter.in.rest.response.BaseResponse;
import com.swissmedical.patients.infrastructure.adapter.in.rest.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PatientNotFoundException.class)
  public ResponseEntity<BaseResponse<?>> handlePatientNotFoundException(PatientNotFoundException ex) {
    return new ResponseEntity<>(new ErrorResponse<>(ex.getMessage()), HttpStatus.NO_CONTENT);
  }

  @ExceptionHandler(PatientDuplicateException.class)
  public ResponseEntity<BaseResponse<?>> handlePatientDuplicateException(PatientDuplicateException ex) {
    return new ResponseEntity<>(new ErrorResponse<>(ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> errorMessages = extractValidationMessages(ex.getBindingResult().getFieldErrors());
    String combinedMessage = String.join("; ", errorMessages);
    return new ResponseEntity<>(new ErrorResponse<>(combinedMessage), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<BaseResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<>(new ErrorResponse<>("Invalid argument: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<?>> handleGenericException(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse<>("Unexpected error: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private List<String> extractValidationMessages(List<FieldError> fieldErrors) {
    return fieldErrors.stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();
  }

}
