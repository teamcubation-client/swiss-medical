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


    private static final Long id = 1L;
    private static final String dni = "12345678";
    private static final String nombre = "Ana";
    private static final String apellido = "Lopez";

    private static final Long idOtro = 2L;
    private static final String dniOtro = "12345678";
    private static final String nombreOtro = "Bruno";
    private static final String apellidoOtro = "Gomez";

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(id)
                .dni(dni)
                .nombre(nombre)
                .apellido(apellido)
                .estado(false)
                .build();

        mismoPaciente = Paciente.builder()
                .id(id)
                .dni(dni)
                .nombre(nombre)
                .apellido(apellido)
                .estado(true)
                .build();

        otroPaciente = Paciente.builder()
                .id(idOtro)
                .dni(dniOtro)
                .nombre(nombreOtro)
                .apellido(apellidoOtro)
                .build();
    }

    @Test
    void validate_givenDniNotExists_doesNotThrow() {
        when(portOutRead.buscarByDni(dni))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validate(paciente));
        verify(portOutRead).buscarByDni(dni);
    }

    @Test
    void validate_givenSamePaciente_doesNotThrow() {
        when(portOutRead.buscarByDni(dni))
                .thenReturn(Optional.of(mismoPaciente));

        assertDoesNotThrow(() -> validator.validate(paciente));
        verify(portOutRead).buscarByDni(dni);
    }

    @Test
    void  validate_givenDifferentPacienteSameDni_throwsPacienteDuplicadoException(){
        when(portOutRead.buscarByDni(dni))
                .thenReturn(Optional.of(otroPaciente));

        PacienteDuplicadoException ex = assertThrows(
                PacienteDuplicadoException.class,
                () -> validator.validate(paciente)
        );
        assertTrue(ex.getMessage().contains(dni));
        verify(portOutRead).buscarByDni(dni);
    }



    @Test
    void validate_withNextValidator_delegatesToNext(){
        PacienteValidator nextValidator = mock(PacienteValidator.class);

        validator.setNext(nextValidator);

        when(portOutRead.buscarByDni(dni))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
