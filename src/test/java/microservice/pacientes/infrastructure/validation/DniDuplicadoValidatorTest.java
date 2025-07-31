package microservice.pacientes.infrastructure.validation;


import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.validation.PacienteValidator;
import microservice.pacientes.shared.PacienteDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DniDuplicadoValidatorTest {

    @Mock
    private PacientePortOutRead portOutRead;

    @InjectMocks
    private DniDuplicadoValidator validator;
    private Paciente paciente;
    private Paciente mismoPaciente;
    private Paciente otroPaciente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void validador_NoExisteDni_NoLanzaException() {
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validate(paciente));
        verify(portOutRead).buscarByDni("12345678");
    }

    @Test
    void validador_MismoDni_NoLanzaException() {
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(mismoPaciente));

        assertDoesNotThrow(() -> validator.validate(paciente));
        verify(portOutRead).buscarByDni("12345678");
    }

    @Test
    void validador_OtroPacienteSameDni_LanzaDuplicado() {
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
    void validador_DelegarSiguienteCadena() {

        PacienteValidator nextValidator = mock(PacienteValidator.class);

        validator.setNext(nextValidator);

        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validate(paciente));

        verify(nextValidator).validate(paciente);
    }

}
