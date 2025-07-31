package microservice.pacientes.unit.application.domain.command.mapper;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.mapper.CreatePacienteMapper;
import microservice.pacientes.application.domain.factory.PacienteFactory;
import microservice.pacientes.application.domain.model.Paciente;
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
@DisplayName("CreatePacienteMapper Tests")
public class CreatePacienteMapperTest {

    @Mock
    private PacienteFactory pacienteFactory;

    @InjectMocks
    private CreatePacienteMapper createPacienteMapper;

    @ParameterizedTest
    @MethodSource("pacientesProvider")
    @DisplayName("Debería mapear correctamente el paciente con distintos datos")
    void shouldMapPacienteCorrectly_whenMapValidPaciente(CreatePacienteCommand command, Paciente expectedPaciente) {
        when(pacienteFactory.create(
                command.getDni(),
                command.getNombre(),
                command.getApellido(),
                command.getObraSocial(),
                command.getEmail(),
                command.getTelefono())
        ).thenReturn(expectedPaciente);

        Paciente paciente = createPacienteMapper.toEntity(command);

        assertEquals(expectedPaciente.getDni(), paciente.getDni());
        assertEquals(expectedPaciente.getNombre(), paciente.getNombre());
        assertEquals(expectedPaciente.getApellido(), paciente.getApellido());
        assertEquals(expectedPaciente.getObraSocial(), paciente.getObraSocial());
        assertEquals(expectedPaciente.getEmail(), paciente.getEmail());
        assertEquals(expectedPaciente.getTelefono(), paciente.getTelefono());
        verify(pacienteFactory, times(1)).create(command.getDni(), command.getNombre(), command.getApellido(), command.getObraSocial(), command.getEmail(), command.getTelefono());
    }

    static Stream<Arguments> pacientesProvider() {
        return Stream.of(
                Arguments.of(
                        new CreatePacienteCommand("12345678", "John", "Doe", "OSDE", "email@example", "123456789"),
                        new Paciente("12345678", "John", "Doe", "OSDE", "email@example", "123456789")
                ),
                Arguments.of(
                        new CreatePacienteCommand("87654321", "Jane", "Smith", "Medife", "jane@ex.com", "987654321"),
                        new Paciente("87654321", "Jane", "Smith", "Medife", "jane@ex.com", "987654321")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPacientesProvider")
    @DisplayName("Debería lanzar una excepción si el paciente es inválido")
    void shouldThrowException_whenMapInvalidPaciente(CreatePacienteCommand command) {
        when(pacienteFactory.create(
                command.getDni(),
                command.getNombre(),
                command.getApellido(),
                command.getObraSocial(),
                command.getEmail(),
                command.getTelefono())
        ).thenThrow(PacienteArgumentoInvalido.class);

        assertThrows(PacienteArgumentoInvalido.class,
                () -> createPacienteMapper.toEntity(command));
        verify(pacienteFactory, times(1)).create(command.getDni(), command.getNombre(), command.getApellido(), command.getObraSocial(), command.getEmail(), command.getTelefono());
    }

    static Stream<CreatePacienteCommand> invalidPacientesProvider() {
        return Stream.of(
                new CreatePacienteCommand("1", "Jo hn", "Doe#", "OSDE", "emailexample", "123456334789"),
                new CreatePacienteCommand("", "Juan", "Pérez", "OSDE", "email@example.com", "123456"),
                new CreatePacienteCommand("1234", "María", "López$", "Medife", "mail@", "987654321"),
                new CreatePacienteCommand("5678", "José", "Gómez", "OSDE", "emailexample", "phone123")
        );
    }
}
