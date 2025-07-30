package com.tq.pacientes.unit.shared.exceptions;

import com.tq.pacientes.infrastructure.adapter.in.rest.dto.ErrorDTO;
import com.tq.pacientes.shared.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandlePatientNotFoundException() {
        PatientNotFoundException ex = new PatientNotFoundException(1L);

        ResponseEntity<ErrorDTO> response = handler.handleNotFound(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getBody().status()),
                () -> assertEquals(ex.getMessage(), response.getBody().message())
        );
    }

    @Test
    void shouldHandleDuplicatePatientException() {
        DuplicatePatientException ex = new DuplicatePatientException("12345678");

        ResponseEntity<ErrorDTO> response = handler.handleDuplicate(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status()),
                () -> assertEquals(ex.getMessage(), response.getBody().message())
        );
    }

    @Test
    void shouldHandlePatientDniNotFoundException() {
        PatientDniNotFoundException ex = new PatientDniNotFoundException("12345678");

        ResponseEntity<ErrorDTO> response = handler.handleDniNotFound(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandlePatientAlreadyActiveException() {
        PatientAlreadyActiveException ex = new PatientAlreadyActiveException(1L);

        ResponseEntity<ErrorDTO> response = handler.handleAlreadyActive(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandleMissingBirthDateException() {
        MissingBirthDateException ex = new MissingBirthDateException();

        ResponseEntity<ErrorDTO> response = handler.handleMissingBirthDate(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandleInvalidPatientAgeException() {
        InvalidPatientAgeException ex = new InvalidPatientAgeException(0);

        ResponseEntity<ErrorDTO> response = handler.handleInvalidPatientAge(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandleGenericException() {
        Exception ex = new Exception();

        ResponseEntity<ErrorDTO> response = handler.handleException(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status()),
                () -> assertTrue(response.getBody().message().contains("An unexpected error occurred"))
        );
    }
}
