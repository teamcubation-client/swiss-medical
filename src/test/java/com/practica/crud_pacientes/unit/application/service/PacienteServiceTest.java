package com.practica.crud_pacientes.unit.application.service;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteEventPublisher;
import com.practica.crud_pacientes.application.domain.port.out.PacienteRepositoryPort;
import com.practica.crud_pacientes.application.service.PacienteService;
import com.practica.crud_pacientes.shared.exceptions.PacienteDuplicadoException;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.buildDomain;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {
    @Mock
    private PacienteRepositoryPort pacienteRepository;

    @Mock
    private PacienteEventPublisher pacienteEventPublisher;

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
    void shouldGetAllPacientes() {
        when(pacienteRepository.findAll()).thenReturn(pacientes);

        List<Paciente> returnedPacientes = pacienteService.getPacientes();

        assertEquals(3, returnedPacientes.size());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void shouldGetPacienteById() {
        int id = 1;
        when(pacienteRepository.findById(id)).thenReturn(paciente);

        Paciente foundPaciente = pacienteService.getPacienteById(id);

        assertEquals(paciente, foundPaciente);
        verify(pacienteRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenPacienteNotFound() {
        int id = 13;
        when(pacienteRepository.findById(id)).thenThrow(new PacienteNoEncontradoException());

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.getPacienteById(id);
        });
        verify(pacienteRepository, times(1)).findById(id);
    }

    @Test
    void shouldSavePaciente() {
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
    void shouldThrowDuplicadoExceptionWhenSavedPacienteAlreadyExists() {
        when(pacienteRepository.getPacienteByDni(paciente.getDni())).thenReturn(paciente);

        assertThrows(PacienteDuplicadoException.class, () -> {
            pacienteService.addPaciente(paciente);
        });
        verify(pacienteRepository, times(1)).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, never()).save(paciente);
        verify(pacienteEventPublisher, never()).publishPacienteCreado(paciente);
    }

    @Test
    void shouldUpdatePaciente() {
        String newEmail = "jDoe@gmail.com";
        paciente.setEmail(newEmail);
        when(pacienteRepository.findById(paciente.getId())).thenReturn(paciente);
        when(pacienteRepository.getPacienteByDni(paciente.getDni())).thenReturn(paciente);
        when(pacienteRepository.save(paciente)).thenReturn(paciente);

        Paciente updatedPaciente = pacienteService.updatePaciente(paciente.getId(), paciente);

        assertEquals(newEmail, updatedPaciente.getEmail());
        verify(pacienteRepository, times(1)).findById(paciente.getId());
        verify(pacienteRepository, times(1)).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void shouldThrowNoEncontradoExceptionWhenIdNotFound() {
        int id = 23;
        when(pacienteRepository.findById(id)).thenThrow(new PacienteNoEncontradoException());

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.updatePaciente(id, paciente);
        });
        verify(pacienteRepository, times(1)).findById(id);
        verify(pacienteRepository, never()).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, never()).save(paciente);
    }

    @Test
    void shouldThrowDuplicadoExceptionWhenDniDuplicated() {
        paciente.setId(1);
        paciente.setDni("12121212");
        Paciente pacienteExistenteConMismoDni = new Paciente();
        pacienteExistenteConMismoDni.setId(222);
        pacienteExistenteConMismoDni.setDni("12121212");
        when(pacienteRepository.findById(paciente.getId())).thenReturn(paciente);
        when(pacienteRepository.getPacienteByDni(paciente.getDni())).thenReturn(pacienteExistenteConMismoDni);

        assertThrows(PacienteDuplicadoException.class, () -> {
            pacienteService.updatePaciente(1, paciente);
        });
        assertNotEquals(paciente.getId(), pacienteExistenteConMismoDni.getId());
        verify(pacienteRepository, times(1)).findById(paciente.getId());
        verify(pacienteRepository, times(1)).getPacienteByDni(paciente.getDni());
        verify(pacienteRepository, never()).save(paciente);
    }

    @Test
    void shouldDeletePaciente() {
        when(pacienteRepository.findById(paciente.getId())).thenReturn(paciente);
        doNothing().when(pacienteRepository).deleteById(paciente.getId());
        doNothing().when(pacienteEventPublisher).publishPacienteEliminado(paciente);

        pacienteService.deletePaciente(paciente.getId());

        verify(pacienteRepository, times(1)).findById(paciente.getId());
        verify(pacienteRepository, times(1)).deleteById(paciente.getId());
        verify(pacienteEventPublisher, times(1)).publishPacienteEliminado(paciente);
    }

    @Test
    void shouldThrowNoEncontradoExceptionWhenPacienteNotFoundAndNotDelete() {
        int id = 22;
        when(pacienteRepository.findById(id)).thenReturn(null);

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.deletePaciente(id);
        });
        verify(pacienteRepository, times(1)).findById(id);
        verify(pacienteRepository, never()).deleteById(id);
        verify(pacienteEventPublisher, never()).publishPacienteEliminado(paciente);
    }

    @Test
    void shouldGetPacienteByDni() {
        String dni = "12121212";
        when(pacienteRepository.getPacienteByDni(dni)).thenReturn(paciente);

        Paciente foundPaciente = pacienteService.getPacienteByDni(dni);

        assertEquals(paciente, foundPaciente);
        verify(pacienteRepository, times(1)).getPacienteByDni(dni);
    }

    @Test
    void shouldThrowNoEncontradoExceptionWhenPacienteNotFoundWithDni() {
        String incorrectDni = "11111111";
        when(pacienteRepository.getPacienteByDni(incorrectDni)).thenReturn(null);

        assertThrows(PacienteNoEncontradoException.class, () -> {
            pacienteService.getPacienteByDni(incorrectDni);
        });
        verify(pacienteRepository, times(1)).getPacienteByDni(incorrectDni);
    }

    @Test
    void shouldGetPacientesByNombre() {
        String nombre = "Jane";

        when(pacienteRepository.getPacientesByNombre(nombre.toLowerCase())).thenReturn(pacientes);

        List<Paciente> foundPacientes = pacienteService.getPacientesByName(nombre);

        assertEquals(pacientes.size(), foundPacientes.size());
        verify(pacienteRepository, times(1)).getPacientesByNombre(nombre.toLowerCase());
    }

    @Test
    void shouldGetPacientesByObraSocial() {
        String obraSocial = "Swiss Medical";
        int limite = 3;
        int off = 0;

        when(pacienteRepository.getPacientesByObraSocial(obraSocial, limite, off)).thenReturn(pacientes);

        List<Paciente> foundPacientes = pacienteService.getPacientesByObraSocial(obraSocial, limite, off);

        assertEquals(pacientes.size(), foundPacientes.size());
        verify(pacienteRepository, times(1)).getPacientesByObraSocial(obraSocial, limite, off);
    }
}
