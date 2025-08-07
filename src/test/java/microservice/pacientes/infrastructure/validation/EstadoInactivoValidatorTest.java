package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteActivoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class EstadoInactivoValidatorTest {

    @InjectMocks
    private EstadoInactivoValidator validator;
    private Paciente paciente;

    @Mock
    private LoggerPort logger;


    private static final Long id = 1L;
    private static final String dni = "12345678";
    private static final String nombre = "Ana";
    private static final String apellido = "Lopez";
    private static final String email = "analopez@gmail.com";
    private static final Boolean estado = true;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(id)
                .dni(dni)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .estado(estado)
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
