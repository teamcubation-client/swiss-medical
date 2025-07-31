package microservice.pacientes.unit.application.service;

import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.command.mapper.CreatePacienteMapper;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOut;
import microservice.pacientes.application.domain.port.out.PacienteUpdater;
import microservice.pacientes.application.service.PacienteService;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import microservice.pacientes.shared.exception.PacienteDuplicadoException;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteServiceTest Tests")
public class PacienteServiceTest {

    @Mock
    private PacientePortOut pacientePortOut;

    @Mock
    private PacienteUpdater pacienteUpdater;

    @Mock
    private CreatePacienteMapper createPacienteMapper;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    @DisplayName("Debería crear correctamente un paciente")
    void shouldCreatePacienteSuccessfully_whenCreateValidPaciente() {
        CreatePacienteCommand createPacienteCommand = new CreatePacienteCommand("12345678", "Juan", "Perez", "Obra social", "email@test.com", "123456789");
        Paciente paciente = new Paciente("12345678", "Juan", "Perez", "Obra social", "email@test.com", "123456789");
        when(createPacienteMapper.toEntity(createPacienteCommand)).thenReturn(paciente);
        when(pacientePortOut.existsByDni("12345678")).thenReturn(false);
        when(pacientePortOut.save(paciente)).thenReturn(paciente);

        Paciente pacienteReturn = pacienteService.create(createPacienteCommand);

        verify(pacientePortOut, times(1)).existsByDni("12345678");
        verify(createPacienteMapper, times(1)).toEntity(createPacienteCommand);
        verify(pacientePortOut, times(1)).save(paciente);
        assertEquals(paciente, pacienteReturn);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar crear un paciente duplicado")
    void shouldThrowException_whenCreateExistsPaciente() {
        String dni = "12345678";
        CreatePacienteCommand createPacienteCommand = new CreatePacienteCommand(dni, "Juan", "Perez", "Obra social", "email@test.com", "123456789");
        Paciente paciente = new Paciente(dni, "Juan", "Perez", "Obra social", "email@test.com", "123456789");
        when(createPacienteMapper.toEntity(createPacienteCommand)).thenReturn(paciente);
        when(pacientePortOut.existsByDni(dni)).thenReturn(true);

        assertThrows(PacienteDuplicadoException.class, () -> pacienteService.create(createPacienteCommand));
        verify(pacientePortOut, times(1)).existsByDni(dni);
        verify(createPacienteMapper, times(1)).toEntity(createPacienteCommand);
    }

    @ParameterizedTest
    @MethodSource("invalidCreatePacienteCommands")
    @DisplayName("Debería lanzar una excepción al intentar crear un paciente con datos inválidos")
    void shouldThrowException_whenCreateInvalidPaciente(CreatePacienteCommand createPacienteCommand) {
        doThrow(new PacienteArgumentoInvalido(""))
                .when(createPacienteMapper).toEntity(createPacienteCommand);

        assertThrows(PacienteArgumentoInvalido.class, () -> pacienteService.create(createPacienteCommand));
        verify(createPacienteMapper, times(1)).toEntity(createPacienteCommand);
    }

    static Stream<CreatePacienteCommand> invalidCreatePacienteCommands() {
        return Stream.of(
                new CreatePacienteCommand("1", "Juan", "Perez", "Obra social", "email@test.com", "123456789"),
                new CreatePacienteCommand("12345678", "", "Perez", "OSDE", "email@example.com", "123456789"),
                new CreatePacienteCommand("12345678", "Juan", "", "OSDE", "email@example.com", "123456789"),
                new CreatePacienteCommand("12345678", "Juan", "Perez", "OSDE", "invalidemail", "123456789"),
                new CreatePacienteCommand("12345678", "Juan", "Perez", "OSDE", "email@example.com", ""),
                new CreatePacienteCommand("", "", "", "", "", "")
        );
    }

    @Test
    @DisplayName("Debería eliminar correctamente un paciente")
    void shouldDeletePacienteSuccessfully_whenDeleteExistsPaciente() {
        String dni = "12345678";
        Paciente paciente = new Paciente(dni, "Juan", "Perez", "Obra social", "email@test.com", "123456789");
        when(pacientePortOut.getByDni(dni)).thenReturn(Optional.of(paciente));

        pacienteService.delete(dni);

        verify(pacientePortOut, times(1)).getByDni(dni);
        verify(pacientePortOut, times(1)).delete(paciente);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar eliminar un paciente inexistente")
    void shouldThrowException_whenDeleteNoExistsPaciente() {
        String dni = "12345678";
        when(pacientePortOut.getByDni(dni)).thenReturn(Optional.empty());

        assertThrows(PacienteNoEncontradoException.class, () -> pacienteService.delete(dni));
        verify(pacientePortOut, times(1)).getByDni(dni);
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void shouldReturnAllPacientes_whenGetAll() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(new Paciente("12345678", "Juan", "Perez", "Obra social", "email@test.com", "123456789"));
        pacientes.add(new Paciente("12345679", "Juan", "Perez", "Obra social", "email@test.com", "123456789"));
        when(pacientePortOut.getAll()).thenReturn(pacientes);

        List<Paciente> pacientesReturn = pacienteService.getAll();

        verify(pacientePortOut, times(1)).getAll();
        assertEquals(pacientes, pacientesReturn);
    }

    @Test
    @DisplayName("Debería obtener correctamente una lista vacía de pacientes")
    void shouldReturnEmptyList_whenGetAllEmpty() {
        List<Paciente> pacientes = new ArrayList<>();
        when(pacientePortOut.getAll()).thenReturn(pacientes);

        List<Paciente> pacientesReturn = pacienteService.getAll();

        verify(pacientePortOut, times(1)).getAll();
        assertEquals(pacientes, pacientesReturn);
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void shouldReturnFilteredPacientes_whenGetByNombreContainingIgnoreCase() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(new Paciente("12345679", "Juan", "Perez", "Obra social", "email@test.com", "123456789"));
        pacientes.add(new Paciente("12345679", "Juana", "Perez", "Obra social", "email@test.com", "123456789"));
        when(pacientePortOut.getByNombreContainingIgnoreCase("Juan")).thenReturn(pacientes);

        List<Paciente> pacientesReturn = pacienteService.getByNombreContainingIgnoreCase("Juan");

        verify(pacientePortOut, times(1)).getByNombreContainingIgnoreCase("Juan");
        assertEquals(pacientes, pacientesReturn);
    }

    @Test
    @DisplayName("Debería obtener correctamente una lista vacía de pacientes")
    void shouldReturnEmptyList_whenGetByNombreContainingIgnoreCaseEmpty() {
        List<Paciente> pacientes = new ArrayList<>();
        when(pacientePortOut.getByNombreContainingIgnoreCase("Juan")).thenReturn(pacientes);

        List<Paciente> pacientesReturn = pacienteService.getByNombreContainingIgnoreCase("Juan");

        verify(pacientePortOut, times(1)).getByNombreContainingIgnoreCase("Juan");
        assertEquals(pacientes, pacientesReturn);
    }

    @Test
    @DisplayName("Debería obtener una lista vacía si no coincide el nombre con los pacientes")
    void shouldReturnEmptyList_whenGetByNombreContainingIgnoreCaseNoMatch() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(new Paciente("12345678", "Juan", "Perez", "Obra social", "email@test.com", "123456789"));
        when(pacientePortOut.getByNombreContainingIgnoreCase("Agustin")).thenReturn(List.of());

        List<Paciente> pacientesReturn = pacienteService.getByNombreContainingIgnoreCase("Agustin");

        verify(pacientePortOut, times(1)).getByNombreContainingIgnoreCase("Agustin");
        assertEquals(0, pacientesReturn.size());
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void shouldReturnPaciente_whenGetByDni() {
        String dni = "12345678";
        Paciente paciente = new Paciente(dni, "Juan", "Perez", "Obra social", "email@test.com", "123456789");
        when(pacientePortOut.getByDni(dni)).thenReturn(Optional.of(paciente));

        Paciente pacienteReturn = pacienteService.getByDni(dni);

        verify(pacientePortOut, times(1)).getByDni(dni);
        assertEquals(paciente, pacienteReturn);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente")
    void shouldThrowException_whenGetByNoExistsDni() {
        String dni = "12345678";
        when(pacientePortOut.getByDni(dni)).thenReturn(Optional.empty());

        assertThrows(PacienteNoEncontradoException.class, () -> pacienteService.getByDni(dni));
        verify(pacientePortOut, times(1)).getByDni(dni);
    }

    @Test
    @DisplayName("Debería obtener correctamente un paciente")
    void shouldReturnPaciente_whenGetByNombre() {
        Paciente paciente = new Paciente("12345678", "Juan", "Perez", "Obra social", "email@test.com", "123456789");
        when(pacientePortOut.getByNombre("Juan")).thenReturn(Optional.of(paciente));

        Paciente pacienteReturn = pacienteService.getByNombre("Juan");

        verify(pacientePortOut, times(1)).getByNombre("Juan");
        assertEquals(paciente, pacienteReturn);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar obtener un paciente inexistente")
    void shouldThrowException_whenGetByNoExistsNombre() {
        when(pacientePortOut.getByNombre("Juan")).thenReturn(Optional.empty());

        assertThrows(PacienteNoEncontradoException.class, () -> pacienteService.getByNombre("Juan"));
        verify(pacientePortOut, times(1)).getByNombre("Juan");
    }

    @Test
    @DisplayName("Debería obtener correctamente los pacientes segun la obra social")
    void shouldReturnPacientesByObraSocial_whenGetByObraSocial() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(new Paciente("12345678", "Juan", "Perez", "OSDE", "email@test.com", "123456789"));
        pacientes.add(new Paciente("12345680", "Agustina", "Perez", "OSDE", "email@test.com", "123456789"));
        when(pacientePortOut.getByObraSocial("OSDE", 10, 0)).thenReturn(pacientes);

        List<Paciente> pacientesReturn = pacienteService.getByObraSocial("OSDE", 10, 0);

        verify(pacientePortOut, times(1)).getByObraSocial("OSDE", 10, 0);
        assertEquals(pacientes, pacientesReturn);
    }

    @Test
    @DisplayName("Debería obtener una lista vacía cuando no haya pacientes con dicha obra social")
    void shouldReturnEmptyList_whenGetByNoExistsObraSocial() {
        List<Paciente> pacientes = new ArrayList<>();
        pacientes.add(new Paciente("12345679", "Agustin", "Perez", "SWISS MEDICAL", "email@test.com", "123456789"));
        when(pacientePortOut.getByObraSocial("OSDE", 10, 0)).thenReturn(List.of());

        List<Paciente> pacientesReturn = pacienteService.getByObraSocial("OSDE", 10, 0);

        verify(pacientePortOut, times(1)).getByObraSocial("OSDE", 10, 0);
        assertEquals(0, pacientesReturn.size());
    }

    @Test
    @DisplayName("Debería actualizar correctamente un paciente")
    void shouldUpdatePacienteSuccessfully_whenUpdateValidPaciente() {
        String dni = "12345678";
        UpdatePacienteCommand command = new UpdatePacienteCommand("Agustin", "Perez", "OSDE", "agustin.perez@gmail.com", "123456789");
        Paciente pacienteExistente = new Paciente(dni, "Juan", "Antiguo", "Medife", "viejo@email.com", "000000000");
        when(pacientePortOut.getByDni(dni)).thenReturn(Optional.of(pacienteExistente));
        doAnswer(invocation -> {
            UpdatePacienteCommand cmd = invocation.getArgument(0);
            Paciente pac = invocation.getArgument(1);
            pac.setNombre(cmd.getNombre());
            pac.setApellido(cmd.getApellido());
            pac.setObraSocial(cmd.getObraSocial());
            pac.setEmail(cmd.getEmail());
            pac.setTelefono(cmd.getTelefono());
            return null;
        }).when(pacienteUpdater).update(command, pacienteExistente);
        when(pacientePortOut.save(pacienteExistente)).thenReturn(pacienteExistente);

        Paciente actualizado = pacienteService.update(dni, command);

        assertEquals("Agustin", actualizado.getNombre());
        assertEquals("Perez", actualizado.getApellido());
        assertEquals("OSDE", actualizado.getObraSocial());
        assertEquals("agustin.perez@gmail.com", actualizado.getEmail());
        assertEquals("123456789", actualizado.getTelefono());
        verify(pacientePortOut, times(1)).getByDni(dni);
        verify(pacienteUpdater, times(1)).update(command, pacienteExistente);
        verify(pacientePortOut, times(1)).save(pacienteExistente);
    }

    @Test
    @DisplayName("Debería lanzar una excepción al intentar actualizar un paciente inexistente")
    void shouldThrowException_whenUpdateNoExistsPaciente() {
        String dni = "12345678";
        UpdatePacienteCommand command = new UpdatePacienteCommand("Juan", "Perez", "OSDE", "juan.perez@gmail.com", "123456789");
        when(pacientePortOut.getByDni(dni)).thenReturn(Optional.empty());

        assertThrows(PacienteNoEncontradoException.class, () -> pacienteService.update(dni, command));
        verify(pacientePortOut, times(1)).getByDni(dni);
    }

    @ParameterizedTest
    @MethodSource("invalidUpdatePacienteCommands")
    @DisplayName("Debería lanzar una excepción al intentar actualizar un paciente con argumentos inválidos")
    void shouldThrowException_whenUpdateInvalidPaciente(UpdatePacienteCommand command) {
        String dni = "12345678";
        Paciente pacienteExistente = new Paciente(dni, "Juan", "Antiguo", "Medife", "viejo@email.com", "000000000");
        when(pacientePortOut.getByDni(dni)).thenReturn(Optional.of(pacienteExistente));
        doThrow(new PacienteArgumentoInvalido("Argumento invalido"))
                .when(pacienteUpdater).update(command, pacienteExistente);

        assertThrows(PacienteArgumentoInvalido.class,
                () -> pacienteService.update(dni, command));
        verify(pacientePortOut, times(1)).getByDni(dni);
    }

    static Stream<UpdatePacienteCommand> invalidUpdatePacienteCommands() {
        return Stream.of(
                new UpdatePacienteCommand(null, "Perez", "OSDE", "juan.perez@gmail.com", "123456789"),
                new UpdatePacienteCommand("Juan", null, "OSDE", "juan.perez@gmail.com", "123456789"),
                new UpdatePacienteCommand("Juan", "Perez", null, "juan.perez@gmail.com", "123456789"),
                new UpdatePacienteCommand("Juan", "Perez", "OSDE", "invalidemail", "123456789"),
                new UpdatePacienteCommand("Juan", "Perez", "OSDE", "juan.perez@gmail.com", ""),
                new UpdatePacienteCommand(null, null, null, null, null)
        );
    }

}
