package microservice.pacientes.infrastructure.validation;


import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.LoggerPort;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteDuplicadoException;
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
public class DniDuplicadoValidatorTest {

    @Mock
    private PacientePortOutRead portOutRead;
    @Mock
    private LoggerPort logger;

    @InjectMocks
    private DniDuplicadoValidator validator;
    private Paciente paciente;
    private Paciente mismoPaciente;
    private Paciente otroPaciente;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .estado(false)
                .build();

        mismoPaciente = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .estado(true)
                .build();

        otroPaciente = Paciente.builder()
                .id(2L)
                .dni("12345678")
                .nombre("Bruno")
                .apellido("Gómez")
                .build();
    }

    @Test
    void validate_givenDniNotExists_doesNotThrow() {
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validate(paciente));
        verify(portOutRead).buscarByDni("12345678");
    }

    @Test
    void validate_givenSamePaciente_doesNotThrow() {
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(mismoPaciente));

        assertDoesNotThrow(() -> validator.validate(paciente));
        verify(portOutRead).buscarByDni("12345678");
    }

    @Test
    void  validate_givenDifferentPacienteSameDni_throwsPacienteDuplicadoException(){
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(otroPaciente));

        PacienteDuplicadoException ex = assertThrows(
                PacienteDuplicadoException.class,
                () -> validator.validate(paciente)
        );
        assertTrue(ex.getMessage().contains("12345678"));
        verify(portOutRead).buscarByDni("12345678");
    }



    @Test
    void validate_withNextValidator_delegatesToNext(){
        PacienteValidator nextValidator = mock(PacienteValidator.class);

        validator.setNext(nextValidator);

        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
