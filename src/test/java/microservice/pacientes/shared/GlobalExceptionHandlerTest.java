package microservice.pacientes.shared;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp(){
        handler = new GlobalExceptionHandler();
    }

    @AfterEach
    void tearDown() {
        handler = null;
    }
    @Test
    void handlePacienteNotFound() {
        long pacienteId = 42L;
        PacienteNotFoundException notFoundException = PacienteNotFoundException.porId(pacienteId);

        ResponseEntity<ErrorResponse> notFoundAnswer = handler.handlePacienteNotFound(notFoundException);

        assertThat(notFoundAnswer.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse bodyNotFound = notFoundAnswer.getBody();
        assertThat(bodyNotFound).isNotNull();
        assertThat(bodyNotFound.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(bodyNotFound.getMensaje()).isEqualTo(notFoundException.getMessage());
        assertThat(bodyNotFound.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }
    @Test
    void handlePacienteDuplicado() {
        String dni = "12345678";
        PacienteDuplicadoException pacienteDuplicadoException = new PacienteDuplicadoException(dni);

        ResponseEntity<ErrorResponse> pacienteDuplicadoAnswer = handler.handleBadRequest(pacienteDuplicadoException);

        assertThat(pacienteDuplicadoAnswer.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse bodyDuplicado = pacienteDuplicadoAnswer.getBody();
        assertThat(bodyDuplicado).isNotNull();
        assertThat(bodyDuplicado.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(bodyDuplicado.getMensaje()).isEqualTo(pacienteDuplicadoException.getMessage());
        assertThat(bodyDuplicado.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());

    }

    @Test
    void handlePacienteNull() {
        PacienteNullException nullException = new PacienteNullException();
        ResponseEntity<ErrorResponse> nullAnswer = handler.handleBadRequest(nullException);

        assertThat(nullAnswer.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse nullBody = nullAnswer.getBody();
        assertThat(nullBody).isNotNull();
        assertThat(nullBody.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(nullBody.getMensaje()).isEqualTo(nullException.getMessage());
        assertThat(nullBody.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void handleEmailFormat() {
        String mensaje = "Formato de mail invalido";
        InvalidEmailFormatException invalidEmailFormatException = new InvalidEmailFormatException(mensaje);
        ResponseEntity<ErrorResponse> invalidEmailAnswer = handler.handleBadRequest(invalidEmailFormatException);

        assertThat(invalidEmailAnswer.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse invalidEmailAnswerBodyBody = invalidEmailAnswer.getBody();
        assertThat(invalidEmailAnswerBodyBody).isNotNull();
        assertThat(invalidEmailAnswerBodyBody.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(invalidEmailAnswerBodyBody.getMensaje()).isEqualTo(invalidEmailFormatException.getMessage());
        assertThat(invalidEmailAnswerBodyBody.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void handleFechaAlta() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        InvalidFechaAltaException invalidFechaAltaException = new InvalidFechaAltaException(fecha);
        ResponseEntity<ErrorResponse> invalidFechaAltaAnswer = handler.handleBadRequest(invalidFechaAltaException);

        assertThat(invalidFechaAltaAnswer.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse invalidFechaAltaAnswerBodybody = invalidFechaAltaAnswer.getBody();
        assertThat(invalidFechaAltaAnswerBodybody).isNotNull();
        assertThat(invalidFechaAltaAnswerBodybody.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(invalidFechaAltaAnswerBodybody.getMensaje()).isEqualTo(invalidFechaAltaException.getMessage());
        assertThat(invalidFechaAltaAnswerBodybody.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void handlePacienteActivo() {
        long pacienteId = 1L;
        PacienteActivoException pacienteActivoException= new PacienteActivoException(pacienteId);
        ResponseEntity<ErrorResponse> pacienteActivoAnswer = handler.handleBadRequest(pacienteActivoException);

        assertThat(pacienteActivoAnswer.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponse pacienteActivoAnswerBody = pacienteActivoAnswer.getBody();
        assertThat(pacienteActivoAnswerBody).isNotNull();
        assertThat(pacienteActivoAnswerBody.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(pacienteActivoAnswerBody.getMensaje()).isEqualTo(pacienteActivoException.getMessage());
        assertThat(pacienteActivoAnswerBody.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

}
