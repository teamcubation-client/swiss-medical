package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidFechaAltaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class FechaAltaValidatorTest {

    @InjectMocks
    private FechaAltaValidator validator;

    @Mock
    private LoggerPort logger;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .build();
    }

    @Test
    void validate_givenPastOrTodayFechaAlta_doesNotThrow(){
        paciente.setFechaAlta(LocalDate.now().minusDays(1));
        assertDoesNotThrow(() -> validator.validate(paciente));

        paciente.setFechaAlta(LocalDate.now());
        assertDoesNotThrow(() -> validator.validate(paciente));
    }

    @Test
    void  validate_givenFutureFechaAlta_throwsInvalidFechaAltaException(){
        paciente.setFechaAlta(LocalDate.now().plusDays(4));

        InvalidFechaAltaException ex = assertThrows(
                InvalidFechaAltaException.class,
                () -> validator.validate(paciente)
        );

        assertEquals(
                InvalidFechaAltaException.MESSAGE,
                ex.getMessage()
        );
    }

    @Test
    void  validate_withNextValidator_delegatesToNext() {
        paciente.setFechaAlta(LocalDate.now());

        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
