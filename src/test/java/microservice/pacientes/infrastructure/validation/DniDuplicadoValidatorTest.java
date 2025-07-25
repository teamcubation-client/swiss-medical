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

    //Crea un mock del puerto de lectura, para controlar cuando existe un paciente en bd
    @Mock
    private PacientePortOutRead portOutRead;

    //InjectMocks construye una instancia real de dniduplicadovalidator y le inyecta porOutRead mockeado
    @InjectMocks
    private DniDuplicadoValidator validator;
    private Paciente paciente;

    //Before each corre este metodo antes de cada test
    @BeforeEach
    void setUp() {
        //openMock inicializa los mocks y el objeto InjectMock
        MockitoAnnotations.openMocks(this);
        //creamos al Paciente con dni 12345678
        paciente = Paciente.builder()
                .id(1L)
                .dni("12345678")
                .nombre("Ana")
                .apellido("Lopez")
                .build();
    }


    //Primer Test: Caso de DNI duplicado,tiene que lanzar excepcion
    @Test
    void validate_shouldThrow_whenDniExists() {
        // Configuramos el mock para que buscarByDni devuelva un Optional con el paciente, simulando que ya hay un paciente registrado con ese DNI.
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.of(paciente));

        // Ejecutamos la validacion y comprobamos que lance PacienteDuplicadoException
        PacienteDuplicadoException ex = assertThrows(
                PacienteDuplicadoException.class,
                () -> validator.validate(paciente)
        );

        // Verificamos que el mensaje de la excepción incluya el DNI duplicado
        assertTrue(ex.getMessage().contains("12345678"));

        // Aseguramos que el mock fue invocado exactamente con el DNI esperado
        verify(portOutRead).buscarByDni("12345678");
    }

    //Segundo test:  Caso exitoso sin duplicado, no debe lanzar excepcion
    @Test
    void validate_shouldPass_whenDniNotExists() {
        // Configuramos el mock para que buscarByDni devuelva Optional.empty(), simulando que no existe ningun paciente con ese DNI
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        // Ejecutamos la validacion y comprobamos que NO lance ninguna excepcion
        assertDoesNotThrow(() -> validator.validate(paciente));

        // Verificamos que se llamo al mock con el DNI correcto
        verify(portOutRead).buscarByDni("12345678");
    }

    //Tercer test: la cadena delega al siguiente
    @Test
    void validate_shouldDelegateToNextValidator_whenNextIsPresent() {
        //Creamos un mock de PacienteValidator que actua como el siguiente handler en la cadena
        PacienteValidator nextValidator = mock(PacienteValidator.class);

        //Asignamos al validator
        validator.setNext(nextValidator);

        // Simulamos que no hay duplicado para pasar la primera validacion
        when(portOutRead.buscarByDni("12345678"))
                .thenReturn(Optional.empty());

        // Ejecutamos la validacion, no debe lanzar ninguna excepcion
        assertDoesNotThrow(() -> validator.validate(paciente));

        // Verificamos la delegacion
        verify(nextValidator).validate(paciente);
    }

}
