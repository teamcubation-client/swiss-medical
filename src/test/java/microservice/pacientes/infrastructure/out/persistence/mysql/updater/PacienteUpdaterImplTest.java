package microservice.pacientes.infrastructure.out.persistence.mysql.updater;

import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.updater.MapStructPacienteUpdater;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.updater.PacienteUpdaterImpl;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteUpdaterImplTest {

    @Spy
    private MapStructPacienteUpdater mapStructPacienteUpdater =
            Mappers.getMapper(MapStructPacienteUpdater.class);

    @Mock
    private PacienteValidator pacienteValidator;

    @InjectMocks
    private PacienteUpdaterImpl pacienteUpdater;

    @Test
    void updateValidPaciente() {
        UpdatePacienteCommand command = new UpdatePacienteCommand("Agustin", "Gonzalez", "Swiss Medical", "agus@gmail.com", "123123123");
        Paciente paciente = new Paciente("12345678", "Juan", "Perez", "OSDE", "juan.perez@gmail.com", "123456789");
        pacienteUpdater.update(command, paciente);

        verify(mapStructPacienteUpdater).update(command, paciente);
        verify(pacienteValidator).validate(paciente);
        assertEquals(command.getNombre(), paciente.getNombre());
        assertEquals(command.getApellido(), paciente.getApellido());
        assertEquals(command.getObraSocial(), paciente.getObraSocial());
        assertEquals(command.getEmail(), paciente.getEmail());
        assertEquals(command.getTelefono(), paciente.getTelefono());
    }

    @Test
    void updateInvalidPaciente() {
        UpdatePacienteCommand command = new UpdatePacienteCommand("J1an", "Pere3z", "OSDE", "juan.perezgmailcom", "123123123456789");
        Paciente paciente = new Paciente("12345678", "Juan", "Perez", "OSDE", "juan.perez@gmail.com", "123456789");

        doThrow(new PacienteArgumentoInvalido("Argumento inválido")).when(pacienteValidator).validate(paciente);
        
        assertThrows(RuntimeException.class, () -> pacienteUpdater.update(command, paciente));
        
        verify(mapStructPacienteUpdater).update(command, paciente);
        verify(pacienteValidator).validate(paciente);
    }
}