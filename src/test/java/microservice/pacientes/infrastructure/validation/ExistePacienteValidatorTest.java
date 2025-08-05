package microservice.pacientes.infrastructure.validation;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ExistePacienteValidatorTest {

    @Mock
    private PacientePortOutRead portOutRead;
    @Mock
    private LoggerPort logger;
    @InjectMocks
    private ExistePacienteValidator validator;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .build();
    }

    @Test
    void validate_givenNonexistentId_throwsPacienteNotFoundException()  {
        when(portOutRead.findById(1L)).thenReturn(Optional.empty());

        PacienteNotFoundException ex = assertThrows(
                PacienteNotFoundException.class,
                () -> validator.validate(paciente)
        );
        assertEquals("Paciente no encontrado con id: 1", ex.getMessage());
        verify(portOutRead).findById(1L);
    }

    @Test
    void validate_givenExistingPaciente_doesNotThrow(){
        when(portOutRead.findById(1L)).thenReturn(Optional.of(paciente));

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(portOutRead).findById(1L);
    }

    @Test
    void validate_withNextValidator_delegatesToNext(){
        when(portOutRead.findById(1L)).thenReturn(Optional.of(paciente));
        PacienteValidator nextValidator = mock(PacienteValidator.class);
        validator.setNext(nextValidator);

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }
}
