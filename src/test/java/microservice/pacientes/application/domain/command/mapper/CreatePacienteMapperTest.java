package microservice.pacientes.application.domain.command.mapper;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.factory.PacienteFactory;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("CreatePacienteMapper Tests")
public class CreatePacienteMapperTest {

    @Mock
    private PacienteFactory pacienteFactory;

    @InjectMocks
    private CreatePacienteMapper createPacienteMapper;

    @Test
    @DisplayName("Debería mapear correctamente el paciente")
    void mapValidPaciente() {
        CreatePacienteCommand command = new CreatePacienteCommand("12345678", "John", "Doe", "OSDE", "email@example", "123456789");
        when(pacienteFactory.create(
                command.getDni(),
                command.getNombre(),
                command.getApellido(),
                command.getObraSocial(),
                command.getEmail(),
                command.getTelefono())
        ).thenReturn(
                new Paciente("12345678", "John", "Doe", "OSDE", "email@example", "123456789"));

        Paciente paciente = createPacienteMapper.toEntity(command);

        assertEquals("12345678", paciente.getDni());
        assertEquals("John", paciente.getNombre());
        assertEquals("Doe", paciente.getApellido());
        assertEquals("OSDE", paciente.getObraSocial());
        assertEquals("email@example", paciente.getEmail());
        assertEquals("123456789", paciente.getTelefono());
    }

    @Test
    @DisplayName("Debería lanzar una excepción si el paciente es inválido")
    void mapInvalidPaciente() {
        CreatePacienteCommand command = new CreatePacienteCommand("1", "Jo hn", "Doe#", "OSDE", "emailexample", "123456334789");
        when(pacienteFactory.create(
                command.getDni(),
                command.getNombre(),
                command.getApellido(),
                command.getObraSocial(),
                command.getEmail(),
                command.getTelefono())
        ).thenThrow(PacienteArgumentoInvalido.class);

        assertThrows(PacienteArgumentoInvalido.class, () -> createPacienteMapper.toEntity(command));
    }
}
