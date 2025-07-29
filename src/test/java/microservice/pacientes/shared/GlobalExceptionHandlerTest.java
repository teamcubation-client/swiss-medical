package microservice.pacientes.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp(){
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handlePacienteNotFound() {
        long pacienteId = 42L;
        PacienteNotFoundException ex = PacienteNotFoundException.porId(pacienteId);
        ResponseEntity<String> resp = handler.handlePacienteNotFound(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isEqualTo(ex.getMessage());
    }

    @Test
    void handlePacienteDuplicado() {
        String dni = "12345678";
        PacienteDuplicadoException ex = new PacienteDuplicadoException(dni);
        ResponseEntity<String> resp = handler.handlePacienteDuplicado(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(ex.getMessage());
    }

    @Test
    void handlePacienteNull() {
        PacienteNullException ex = new PacienteNullException();
        ResponseEntity<String> resp = handler.handlePacienteNull(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(ex.getMessage());
    }

    @Test
    void handleEmailFormat() {
        String mensaje = "Formato de mail invalido";
        InvalidEmailFormatException ex = new InvalidEmailFormatException(mensaje);
        ResponseEntity<String> resp = handler.handleEmailFormat(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(ex.getMessage());
    }

    @Test
    void handleFechaAlta() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        InvalidFechaAltaException ex = new InvalidFechaAltaException(fecha);
        ResponseEntity<String> resp = handler.handleFechaAlta(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(ex.getMessage());
    }

    @Test
    void handlePacienteActivo() {
        long pacienteId=1L;
        PacienteActivoException ex = new PacienteActivoException(pacienteId);
        ResponseEntity<String> resp = handler.handlePacienteActivo(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo(ex.getMessage());
    }

}
