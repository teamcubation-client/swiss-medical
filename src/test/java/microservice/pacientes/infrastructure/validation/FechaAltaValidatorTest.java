package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidFechaAltaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class FechaAltaValidatorTest {

    @InjectMocks
    private FechaAltaValidator validator;
    private Paciente paciente;
    private static final LocalDate FIXED_DATE = LocalDate.of(2025, 7, 30);

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .build();
    }

    @Test
    void validate_givenNullFechaAlta_doesNotThrow() {
        paciente.setFechaAlta(null);

        assertDoesNotThrow(() -> validator.validate(paciente));
    }

    @Test
    void validate_givenPastOrTodayFechaAlta_doesNotThrow(){
        paciente.setFechaAlta(FIXED_DATE.minusDays(1));
        assertDoesNotThrow(() -> validator.validate(paciente));

        paciente.setFechaAlta(FIXED_DATE);
        assertDoesNotThrow(() -> validator.validate(paciente));
    }

    @Test
    void  validate_givenFutureFechaAlta_throwsInvalidFechaAltaException(){
        LocalDate fechaFutura = FIXED_DATE.plusDays(2);
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
    void  validate_withNextValidator_delegatesToNext() {
        paciente.setFechaAlta(FIXED_DATE);

        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
