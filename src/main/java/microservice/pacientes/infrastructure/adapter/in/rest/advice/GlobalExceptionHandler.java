package microservice.pacientes.infrastructure.adapter.in.rest.advice;

import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import microservice.pacientes.shared.exception.PacienteDuplicadoException;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PacienteNoEncontradoException.class)
    public ResponseEntity<ApiError> handlePacienteNoEncontrado(PacienteNoEncontradoException ex) {
        ApiError error = new ApiError(ex.getMessage(), 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PacienteDuplicadoException.class)
    public ResponseEntity<ApiError> handlePacienteDuplicado(PacienteDuplicadoException ex) {
        ApiError error = new ApiError(ex.getMessage(), 409);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PacienteArgumentoInvalido.class)
    public ResponseEntity<ApiError> handlePacienteArgumentoInvalido(PacienteArgumentoInvalido ex) {
        ApiError error = new ApiError(ex.getMessage(), 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
        String mensaje = String.format("Campo '%s': %s", fieldError.getField(), fieldError.getDefaultMessage());

        ApiError error = new ApiError(mensaje, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        ApiError error = new ApiError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

