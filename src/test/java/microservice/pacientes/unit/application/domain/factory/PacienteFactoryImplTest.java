package microservice.pacientes.unit.application.domain.factory;

import microservice.pacientes.application.domain.factory.PacienteFactoryImpl;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteFactoryImpl Tests")
public class PacienteFactoryImplTest {

    @Mock
    private PacienteValidator pacienteValidator;

    @InjectMocks
    private PacienteFactoryImpl pacienteFactory;

    @ParameterizedTest
    @MethodSource("validPacientesProvider")
    @DisplayName("Debería crear un paciente válido")
    void createValidPaciente(String dni, String nombre, String apellido, String obraSocial, String email, String telefono) {
        Paciente paciente = pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);

        assertEquals(dni, paciente.getDni());
        assertEquals(nombre, paciente.getNombre());
        assertEquals(apellido, paciente.getApellido());
        assertEquals(obraSocial, paciente.getObraSocial());
        assertEquals(email, paciente.getEmail());
        assertEquals(telefono, paciente.getTelefono());
        verify(pacienteValidator, times(1)).validate(paciente);
    }

    static Stream<Arguments> validPacientesProvider() {
        return Stream.of(
                Arguments.of("12345678", "Juan", "Perez", "OSDE", "juan.perez@gmail.com", "123456789")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPacienteArgumentsProvider")
    void createPacienteInvalidCases(
            String dni,
            String nombre,
            String apellido,
            String obraSocial,
            String email,
            String telefono
    ) {
        doThrow(new PacienteArgumentoInvalido("Argumento invalido"))
                .when(pacienteValidator)
                .validate(any(Paciente.class));

        PacienteArgumentoInvalido exception = assertThrows(
                PacienteArgumentoInvalido.class,
                () -> pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono)
        );

        assertEquals("Argumento invalido", exception.getMessage());

        verify(pacienteValidator, times(1)).validate(any(Paciente.class));
    }

    static Stream<Arguments> invalidPacienteArgumentsProvider() {
        return Stream.of(
                Arguments.of("1111", "Juan", "Perez", "OSDE", "juan.perez@gmail.com", "123456789"),  // dni inválido
                Arguments.of("12345678", "", "Perez", "OSDE", "juan.perez@gmail.com", "123456789"),       // nombre inválido
                Arguments.of("12345678", "Juan", "", "OSDE", "juan.perez@gmail.com", "123456789"),        // apellido inválido
                Arguments.of("12345678", "Juan", "Perez", "OSDE", "juan.perez", "123456789"),             // email inválido
                Arguments.of("12345678", "Juan", "Perez", "OSDE", "juan.perez@gmail.com", ""),             // teléfono inválido (vacío)
                Arguments.of("1", "", "", "OSDE", "juan.perez", "123")                                   // múltiples inválidos
        );
    }
}
