package com.practica.crud_pacientes.unit.application.service;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteEventPublisher;
import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.application.service.PacienteService;
import com.practica.crud_pacientes.shared.exceptions.PacienteDuplicadoException;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.buildDomain;
import static com.practica.crud_pacientes.utils.TestConstants.LIMITE;
import static com.practica.crud_pacientes.utils.TestConstants.OBRA_SOCIAL;
import static com.practica.crud_pacientes.utils.TestConstants.NOMBRE;
import static com.practica.crud_pacientes.utils.TestConstants.OFF;
import static com.practica.crud_pacientes.utils.TestConstants.DNI;
import static com.practica.crud_pacientes.utils.TestConstants.ID;
import static com.practica.crud_pacientes.utils.TestConstants.NEW_EMAIL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;



@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {
    @Mock
    private PacienteRepositoryPort pacienteRepository;

    @Mock
    private PacienteEventPublisher pacienteEventPublisher;

    @Mock
    private PacienteLoggerPort loggerPort;

    @InjectMocks
    private PacienteService pacienteService;
    private Paciente paciente;
    private List<Paciente> pacientes;

    @BeforeEach
    void setUp() {
        this.paciente = buildDomain();

        pacientes = List.of(new Paciente(), new Paciente(), new Paciente());
    }

    @Test
    @DisplayName("Should get all pacientes")
    void shouldGetAllPacientes() {
        when(pacienteRepository.findAll()).thenReturn(pacientes);

        List<Paciente> returnedPacientes = pacienteService.getPacientes();

        assertEquals(3, returnedPacientes.size());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get paciente by ID when valid ID is given")
    void shouldGetPacienteById_whenValidIdIsGiven() {
        when(pacienteRepository.findById(ID)).thenReturn(paciente);

        Paciente foundPaciente = pacienteService.getPacienteById(ID);

        assertEquals(paciente, foundPaciente);
        verify(pacienteRepository, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Should throw exception when paciente not found with given ID")
    void shouldThrowException_whenPacienteNotFoundWithGivenId() {
        when(pacienteRepository.findById(ID)).thenThrow(new PacienteNoEncontradoException());

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.getPacienteById(ID);
        });
        verify(pacienteRepository, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Should save paciente when all arguments are valid")
    void shouldSavePaciente_whenAllArgumentsAreValid() {
        when(pacienteRepository.getPacienteByDni(paciente.getDni())).thenReturn(null);
        when(pacienteRepository.save(paciente)).thenReturn(paciente);
        doNothing().when(pacienteEventPublisher).publishPacienteCreado(paciente);

        Paciente newPaciente = pacienteService.addPaciente(paciente);

        assertEquals(paciente, newPaciente);
        verify(pacienteRepository, times(1)).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, times(1)).save(paciente);
        verify(pacienteEventPublisher, times(1)).publishPacienteCreado(paciente);
    }

    @Test
    @DisplayName("Should throw duplicado exception when saved paciente already exists")
    void shouldThrowDuplicadoException_whenSavedPacienteAlreadyExists() {
        when(pacienteRepository.getPacienteByDni(paciente.getDni())).thenReturn(paciente);

        assertThrows(PacienteDuplicadoException.class, () -> {
            pacienteService.addPaciente(paciente);
        });
        verify(pacienteRepository, times(1)).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, never()).save(paciente);
        verify(pacienteEventPublisher, never()).publishPacienteCreado(paciente);
    }

    @Test
    @DisplayName("Should update paciente when paciente exists and DNI is not duplicated")
    void shouldUpdatePaciente_whenPacienteExistsAndDniIsNotDuplicated() {
        paciente.setEmail(NEW_EMAIL);
        when(pacienteRepository.findById(paciente.getId())).thenReturn(paciente);
        when(pacienteRepository.getPacienteByDni(paciente.getDni())).thenReturn(paciente);
        when(pacienteRepository.save(paciente)).thenReturn(paciente);

        Paciente updatedPaciente = pacienteService.updatePaciente(paciente.getId(), paciente);

        assertEquals(NEW_EMAIL, updatedPaciente.getEmail());
        verify(pacienteRepository, times(1)).findById(paciente.getId());
        verify(pacienteRepository, times(1)).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    @DisplayName("Should throw PacienteNoEncontradoException when paciente ID does not exist")
    void shouldThrowPacienteNoEncontradoException_whenPacienteIdDoesNotExist() {
        when(pacienteRepository.findById(ID)).thenThrow(new PacienteNoEncontradoException());

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.updatePaciente(ID, paciente);
        });
        verify(pacienteRepository, times(1)).findById(ID);
        verify(pacienteRepository, never()).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, never()).save(paciente);
    }

    @Test
    @DisplayName("Should throw PacienteDuplicadoException when DNI belongs to another paciente")
    void shouldThrowPacienteDuplicadoException_whenDniBelongsToAnotherPaciente() {
        paciente.setId(ID);
        paciente.setDni(DNI);
        Paciente pacienteExistenteConMismoDni = new Paciente();
        pacienteExistenteConMismoDni.setId(12);
        pacienteExistenteConMismoDni.setDni(DNI);
        when(pacienteRepository.findById(paciente.getId())).thenReturn(paciente);
        when(pacienteRepository.getPacienteByDni(paciente.getDni())).thenReturn(pacienteExistenteConMismoDni);

        assertThrows(PacienteDuplicadoException.class, () -> {
            pacienteService.updatePaciente(ID, paciente);
        });
        assertNotEquals(paciente.getId(), pacienteExistenteConMismoDni.getId());
        verify(pacienteRepository, times(1)).findById(paciente.getId());
        verify(pacienteRepository, times(1)).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, never()).save(paciente);
    }

    @Test
    @DisplayName("Should delete paciente when paciente exists")
    void shouldDeletePaciente_whenPacienteExists() {
        when(pacienteRepository.findById(paciente.getId())).thenReturn(paciente);
        doNothing().when(pacienteRepository).deleteById(paciente.getId());
        doNothing().when(pacienteEventPublisher).publishPacienteEliminado(paciente);

        pacienteService.deletePaciente(paciente.getId());

        verify(pacienteRepository, times(1)).findById(paciente.getId());
        verify(pacienteRepository, times(1)).deleteById(paciente.getId());
        verify(pacienteEventPublisher, times(1)).publishPacienteEliminado(paciente);
    }

    @Test
    @DisplayName("Should throw PacienteNoEncontradoException when paciente does not exist on delete")
    void shouldThrowPacienteNoEncontradoException_whenPacienteDoesNotExistOnDelete() {
        when(pacienteRepository.findById(ID)).thenReturn(null);

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.deletePaciente(ID);
        });
        verify(pacienteRepository, times(1)).findById(ID);
        verify(pacienteRepository, never()).deleteById(ID);
        verify(pacienteEventPublisher, never()).publishPacienteEliminado(paciente);
    }

    @Test
    @DisplayName("Should get paciente by DNI when DNI exists")
    void shouldGetPacienteByDni_whenDniExists() {
        when(pacienteRepository.getPacienteByDni(DNI)).thenReturn(paciente);

        Paciente foundPaciente = pacienteService.getPacienteByDni(DNI);

        assertEquals(paciente, foundPaciente);
        verify(pacienteRepository, times(1)).getPacienteByDni(DNI);
    }

    @Test
    @DisplayName("Should throw PacienteNoEncontradoException when DNI does not exist")
    void shouldThrowPacienteNoEncontradoException_whenDniDoesNotExist() {
        when(pacienteRepository.getPacienteByDni(DNI)).thenReturn(null);

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.getPacienteByDni(DNI);
        });
        verify(pacienteRepository, times(1)).getPacienteByDni(DNI);
    }

    @Test
    @DisplayName("Should return pacientes list when nombre matches")
    void shouldReturnPacientesList_whenNombreMatches() {
        when(pacienteRepository.getPacientesByNombre(NOMBRE.toLowerCase())).thenReturn(pacientes);

        List<Paciente> foundPacientes = pacienteService.getPacientesByName(NOMBRE);

        assertEquals(pacientes.size(), foundPacientes.size());
        verify(pacienteRepository, times(1)).getPacientesByNombre(NOMBRE.toLowerCase());
    }

    @Test
    @DisplayName("Should return pacientes list when obra social matches with pagination")
    void shouldReturnPacientesList_whenObraSocialMatchesWithPagination() {
        when(pacienteRepository.getPacientesByObraSocial(OBRA_SOCIAL, LIMITE, OFF)).thenReturn(pacientes);

        List<Paciente> foundPacientes = pacienteService.getPacientesByObraSocial(OBRA_SOCIAL, LIMITE, OFF);

        assertEquals(pacientes.size(), foundPacientes.size());
        verify(pacienteRepository, times(1)).getPacientesByObraSocial(OBRA_SOCIAL, LIMITE, OFF);
    }
}
