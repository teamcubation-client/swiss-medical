package com.tq.pacientes.unit.shared.exceptions;

import com.tq.pacientes.infrastructure.adapter.in.rest.dto.ErrorDTO;
import com.tq.pacientes.shared.exceptions.DuplicatePatientException;
import com.tq.pacientes.shared.exceptions.GlobalExceptionHandler;
import com.tq.pacientes.shared.exceptions.InvalidPatientAgeException;
import com.tq.pacientes.shared.exceptions.MissingBirthDateException;
import com.tq.pacientes.shared.exceptions.PatientAlreadyActiveException;
import com.tq.pacientes.shared.exceptions.PatientDniNotFoundException;
import com.tq.pacientes.shared.exceptions.PatientNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private static final Long PATIENT_ID = 1L;
    private static final String DNI = "12345678";
    private static final String UNEXPECTED_ERROR_MESSAGE = "An unexpected error occurred";

    @Test
    void shouldHandlePatientNotFoundException_whenPatientIsNotFound() {
        PatientNotFoundException ex = new PatientNotFoundException(PATIENT_ID);

        ResponseEntity<ErrorDTO> response = handler.handleNotFound(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getBody().status()),
                () -> assertEquals(ex.getMessage(), response.getBody().message())
        );
    }

    @Test
    void shouldHandleDuplicatePatientException_whenPatientDniIsDuplicated() {
        DuplicatePatientException ex = new DuplicatePatientException(DNI);

        ResponseEntity<ErrorDTO> response = handler.handleDuplicate(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status()),
                () -> assertEquals(ex.getMessage(), response.getBody().message())
        );
    }

    @Test
    void shouldHandlePatientDniNotFoundException_whenPatientDniIsNotFound() {
        PatientDniNotFoundException ex = new PatientDniNotFoundException(DNI);

        ResponseEntity<ErrorDTO> response = handler.handleDniNotFound(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandlePatientAlreadyActiveException_whenPatientIsAlreadyActive() {
        PatientAlreadyActiveException ex = new PatientAlreadyActiveException(PATIENT_ID);

        ResponseEntity<ErrorDTO> response = handler.handleAlreadyActive(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.CONFLICT, response.getStatusCode()),
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandleMissingBirthDateException_whenBirthDateIsNull() {
        MissingBirthDateException ex = new MissingBirthDateException();

        ResponseEntity<ErrorDTO> response = handler.handleMissingBirthDate(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandleInvalidPatientAgeException_whenPatientAgeIsInvalid() {
        InvalidPatientAgeException ex = new InvalidPatientAgeException(0);

        ResponseEntity<ErrorDTO> response = handler.handleInvalidPatientAge(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status())
        );
    }

    @Test
    void shouldHandleGenericException_whenExceptionIsThrown() {
        Exception ex = new Exception();

        ResponseEntity<ErrorDTO> response = handler.handleException(ex);

        assertAll(
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status()),
                () -> assertTrue(response.getBody().message().contains(UNEXPECTED_ERROR_MESSAGE))
        );
    }
}
