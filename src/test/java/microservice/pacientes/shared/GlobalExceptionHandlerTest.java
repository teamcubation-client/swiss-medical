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
        PacienteNotFoundException ex = PacienteNotFoundException.porId(42L);
        ResponseEntity<String> resp = handler.handlePacienteNotFound(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).contains("Paciente no encontrado con id: 42");
    }

    @Test
    void handlePacienteDuplicado() {
        PacienteDuplicadoException ex = new PacienteDuplicadoException("ID");
        ResponseEntity<String> resp = handler.handlePacienteDuplicado(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo("Ya existe un paciente con el DNI: ID");
    }

    @Test
    void handlePacienteNull() {
        PacienteNullException ex = new PacienteNullException();
        ResponseEntity<String> resp = handler.handlePacienteNull(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo("No se pudo mapear, Paciente no puede ser null");
    }

    @Test
    void handleEmailFormat() {
        InvalidEmailFormatException ex = new InvalidEmailFormatException("Formato de mail invalido");
        ResponseEntity<String> resp = handler.handleEmailFormat(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo("Formato de mail invalido");
    }

    @Test
    void handleFechaAlta() {
        LocalDate fecha = LocalDate.now();
        InvalidFechaAltaException ex = new InvalidFechaAltaException(fecha);
        ResponseEntity<String> resp = handler.handleFechaAlta(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo("La fecha de alta no puede ser mayor a la fecha actual");
    }

    @Test
    void handlePacienteActivo() {
        PacienteActivoException ex = new PacienteActivoException(1L);
        ResponseEntity<String> resp = handler.handlePacienteActivo(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo("No se puede eliminar el paciente " + 1L + " porque esta activo");
    }


}
