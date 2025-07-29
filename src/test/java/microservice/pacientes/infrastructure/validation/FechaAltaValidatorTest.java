package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidFechaAltaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FechaAltaValidatorTest {

    @InjectMocks
    private FechaAltaValidator validator;
    private Paciente paciente;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paciente = Paciente.builder()
                .id(1L)
                .build();
    }

    @Test
    void validador_FechaAltaNull() {
        paciente.setFechaAlta(null);

        assertDoesNotThrow(() -> validator.validate(paciente));
    }

    @Test
    void validador_FechaAltaCorrecta() {
        paciente.setFechaAlta(LocalDate.now().minusDays(1));
        assertDoesNotThrow(() -> validator.validate(paciente));

        paciente.setFechaAlta(LocalDate.now());
        assertDoesNotThrow(() -> validator.validate(paciente));
    }

    @Test
    void validador_FechaAltaFutura() {
        LocalDate fechaFutura = LocalDate.now().plusDays(2);
        paciente.setFechaAlta(fechaFutura);

        InvalidFechaAltaException ex = assertThrows(
                InvalidFechaAltaException.class,
                () -> validator.validate(paciente)
        );

        assertEquals(
                "La fecha de alta no puede ser mayor a la fecha actual",
                ex.getMessage()
        );
    }

    @Test
    void validador_DelegarSiguienteCadena() {
        paciente.setFechaAlta(LocalDate.now());

        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
