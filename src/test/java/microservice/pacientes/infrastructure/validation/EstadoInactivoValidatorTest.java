package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteActivoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class EstadoInactivoValidatorTest {

    @InjectMocks
    private EstadoInactivoValidator validator;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .email("analopez@gmail.com")
                .estado(true)
                .build();
    }

    @Test
    void validate_givenActiveEstado_throwsPacienteActivoException(){
        paciente.setEstado(true);

        PacienteActivoException ex = assertThrows(
                PacienteActivoException.class,
                () -> validator.validate(paciente)
        );

        assertTrue(ex.getMessage().contains("1"));
    }

    @Test
    void validate_givenInactiveEstado_doesNotThrow(){
        paciente.setEstado(false);

        assertDoesNotThrow(() -> validator.validate(paciente));
    }

    @Test
    void validate_withNextValidator_delegatesToNext() {
        paciente.setEstado(false);
        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
