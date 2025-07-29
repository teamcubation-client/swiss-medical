package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.InvalidEmailFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EmailFormatValidatorTest {


    @InjectMocks
    private EmailFormatValidator validator;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paciente = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .email("analopez@gmail.com")
                .build();
    }

    @Test
    void validador_EmailValido(){
        paciente.setEmail("usuario@dominio.com");

        assertDoesNotThrow(()-> validator.validate(paciente));
    }

    @Test
    void validador_EmailInvalido(){
        paciente.setEmail("sin-arroba");

        InvalidEmailFormatException ex = assertThrows(
                InvalidEmailFormatException.class,
                () -> validator.validate(paciente)
        );

        assertEquals("Formato de mail invalido",ex.getMessage());
    }

    @Test
    void validador_EmailNulo() {

        paciente.setEmail(null);

        InvalidEmailFormatException ex = assertThrows(
                InvalidEmailFormatException.class,
                () -> validator.validate(paciente)
        );

        assertEquals("Formato de mail invalido", ex.getMessage());
    }

    @Test
    void validador_DelegarSiguienteCadena() {
        paciente.setEmail("usuario@dominio.com");
        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }
}
