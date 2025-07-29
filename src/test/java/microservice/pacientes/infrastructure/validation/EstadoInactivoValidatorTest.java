package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteActivoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EstadoInactivoValidatorTest {

    @InjectMocks
    private EstadoInactivoValidator validator;
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
                .estado(true)
                .build();
    }

    @Test
    void validador_EstadoActivo(){
        paciente.setEstado(true);

        PacienteActivoException ex = assertThrows(
                PacienteActivoException.class,
                () -> validator.validate(paciente)
        );

        assertTrue(ex.getMessage().contains("1"));
    }

    @Test
    void validador_EstadoInactivo() {
        paciente.setEstado(false);

        assertDoesNotThrow(() -> validator.validate(paciente));
    }

    @Test
    void validador_DelegarSiguienteCadena() {
        paciente.setEstado(false);
        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
