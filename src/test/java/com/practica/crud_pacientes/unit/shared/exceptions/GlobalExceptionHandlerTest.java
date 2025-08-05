package com.practica.crud_pacientes.unit.shared.exceptions;

import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
import com.practica.crud_pacientes.shared.exceptions.ErrorResponse;
import com.practica.crud_pacientes.shared.exceptions.GlobalExceptionHandler;
import com.practica.crud_pacientes.shared.exceptions.PacienteDuplicadoException;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static com.practica.crud_pacientes.utils.TestConstants.PATH_BUSCAR_ID;
import static com.practica.crud_pacientes.utils.TestConstants.ENDPOINT;
import static com.practica.crud_pacientes.utils.TestConstants.MENSAJE_EXCEPCION_GENERICA;
import static com.practica.crud_pacientes.utils.TestConstants.ENTITY_NAME;
import static com.practica.crud_pacientes.utils.TestConstants.ERROR_FIELD_1;
import static com.practica.crud_pacientes.utils.TestConstants.ERROR_MSG_1;
import static com.practica.crud_pacientes.utils.TestConstants.ERROR_FIELD_2;
import static com.practica.crud_pacientes.utils.TestConstants.ERROR_MSG_2;
import static com.practica.crud_pacientes.utils.TestConstants.ERROR_MSG_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private PacienteLoggerPort loggerPort;

    @InjectMocks
    private GlobalExceptionHandler handler;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("Should handle PacienteNoEncontradoException when paciente is not found")
    void shouldHandlePacienteNoEncontradoException_whenPacienteIsNotFound() {
        request.setRequestURI(PATH_BUSCAR_ID);
        PacienteNoEncontradoException exception = new PacienteNoEncontradoException();

        ErrorResponse response = handler.handlePacienteNoEncontrado(exception, request);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getMensaje()).isEqualTo(exception.getMessage());
        assertThat(response.getPath()).isEqualTo(request.getRequestURI());
        assertThat(response.getError()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @Test
    @DisplayName("Should handle generic exception when unexpected exception occurs")
    void shouldHandleGenericException_whenUnexpectedExceptionOccurs() {
        request.setRequestURI(ENDPOINT);
        Exception exception = new RuntimeException(MENSAJE_EXCEPCION_GENERICA);

        ErrorResponse response = handler.handleExcepcionGenerica(exception, request);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getMensaje()).contains(exception.getMessage());
        assertThat(response.getPath()).isEqualTo(ENDPOINT);
        assertThat(response.getError()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @Test
    @DisplayName("Should handle validation errors when request has invalid fields")
    void shouldHandleValidationErrors_whenRequestHasInvalidFields() {
        request.setRequestURI(ENDPOINT);
        FieldError fieldError1 = new FieldError(ENTITY_NAME, ERROR_FIELD_1, ERROR_MSG_1);
        FieldError fieldError2 = new FieldError(ENTITY_NAME, ERROR_FIELD_2, ERROR_MSG_2);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ErrorResponse response = handler.handleValidationsException(exception, request);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getMensaje()).isEqualTo(ERROR_MSG_EXCEPTION);
        assertThat(response.getPath()).isEqualTo(ENDPOINT);
        assertThat(response.getError()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());

        Map<String, String> errores = response.getErrores();
        assertThat(errores)
                .containsEntry(ERROR_FIELD_1, ERROR_MSG_1)
                .containsEntry(ERROR_FIELD_2, ERROR_MSG_2);
    }

    @Test
    @DisplayName("Should handle PacienteDuplicadoException when paciente with same DNI already exists")
    void shouldHandlePacienteDuplicadoException_whenPacienteWithSameDniAlreadyExists() {
        request.setRequestURI(ENDPOINT);
        PacienteDuplicadoException exception = new PacienteDuplicadoException();

        ErrorResponse response = handler.handlePacienteDuplicado(exception, request);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getMensaje()).isEqualTo(exception.getMessage());
        assertThat(response.getPath()).isEqualTo(ENDPOINT);
        assertThat(response.getError()).isEqualTo(HttpStatus.CONFLICT.getReasonPhrase());
    }
}
