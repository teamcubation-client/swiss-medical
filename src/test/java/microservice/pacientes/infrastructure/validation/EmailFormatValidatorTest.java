package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidEmailFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class EmailFormatValidatorTest {


    @InjectMocks
    private EmailFormatValidator validator;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .email("analopez@gmail.com")
                .build();
    }

    @Test
    void validate_givenValidEmail_doesNotThrow(){
        paciente.setEmail("usuario@dominio.com");

        assertDoesNotThrow(()-> validator.validate(paciente));
    }

    @Test
    void validate_givenInvalidEmail_throwsInvalidEmailFormatException(){
        paciente.setEmail("sin-arroba");

        InvalidEmailFormatException ex = assertThrows(
                InvalidEmailFormatException.class,
                () -> validator.validate(paciente)
        );

        assertEquals(InvalidEmailFormatException.MESSAGE,ex.getMessage());
    }

    @Test
    void  validate_givenNullEmail_throwsInvalidEmailFormatException()  {

        paciente.setEmail(null);

        InvalidEmailFormatException ex = assertThrows(
                InvalidEmailFormatException.class,
                () -> validator.validate(paciente)
        );

        assertEquals(InvalidEmailFormatException.MESSAGE, ex.getMessage());
    }

    @Test
    void validate_withNextValidator_delegatesToNext() {
        paciente.setEmail("usuario@dominio.com");
        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }
}
