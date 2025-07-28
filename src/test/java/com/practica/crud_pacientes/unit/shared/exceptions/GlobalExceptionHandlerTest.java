package com.practica.crud_pacientes.unit.shared.exceptions;

import com.practica.crud_pacientes.shared.exceptions.ErrorResponse;
import com.practica.crud_pacientes.shared.exceptions.GlobalExceptionHandler;
import com.practica.crud_pacientes.shared.exceptions.PacienteDuplicadoException;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
    }

    @Test
    void shouldHandlePacienteNoEncontradoException() {
        request.setRequestURI("/pacientes/buscar-por-id/1234");
        PacienteNoEncontradoException exception = new PacienteNoEncontradoException();

        ErrorResponse response = handler.handlerPacienteNoEncontrado(exception, request);

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMensaje()).isEqualTo("El paciente que se intenta buscar no existe.");
        assertThat(response.getPath()).isEqualTo("/pacientes/buscar-por-id/1234");
        assertThat(response.getError()).isEqualTo("Not Found");
    }

    @Test
    void shouldHandleGenericException() {
        request.setRequestURI("/pacientes");
        Exception exception = new RuntimeException("Algo salió mal");

        ErrorResponse response = handler.handlerExcepcionGenerica(exception, request);

        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getMensaje()).contains("Ocurrió un error inesperado: Algo salió mal");
        assertThat(response.getPath()).isEqualTo("/pacientes");
        assertThat(response.getError()).isEqualTo("Internal Server Error");
    }

    @Test
    void shouldHandleValidationErrors() {
        request.setRequestURI("/pacientes/nuevo-paciente");
        FieldError fieldError1 = new FieldError("paciente", "nombre", "El nombre es obligatorio");
        FieldError fieldError2 = new FieldError("paciente", "dni", "El DNI no es válido");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ErrorResponse response = handler.handlerValidationsException(exception, request);

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getMensaje()).isEqualTo("Errores de validacion");
        assertThat(response.getPath()).isEqualTo("/pacientes/nuevo-paciente");
        assertThat(response.getError()).isEqualTo("Bad Request");

        Map<String, String> errores = response.getErrores();
        assertThat(errores)
                .containsEntry("nombre", "El nombre es obligatorio")
                .containsEntry("dni", "El DNI no es válido");
    }

    @Test
    void shouldHandlerPacienteDuplicadoException() {
        request.setRequestURI("/pacientes/nuevo-paciente");
        PacienteDuplicadoException exception = new PacienteDuplicadoException();

        ErrorResponse response = handler.handlerPacienteDuplicado(exception, request);

        assertThat(response.getStatus()).isEqualTo(409);
        assertThat(response.getMensaje()).isEqualTo("Ya existe un paciente con ese DNI.");
        assertThat(response.getPath()).isEqualTo("/pacientes/nuevo-paciente");
        assertThat(response.getError()).isEqualTo("Conflict");
    }
}
