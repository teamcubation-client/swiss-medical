package microservice.pacientes.unit.infrastructure.out.persistence.mysql.repository;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.entity.PacienteEntity;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.mapper.PacienteEntityMapper;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.repository.JpaPacienteRepository;
import microservice.pacientes.infrastructure.adapter.out.persistence.mysql.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteRepositoryTest {

    @Mock
    private JpaPacienteRepository jpaPacienteRepository;

    @Mock
    private PacienteEntityMapper pacienteEntityMapper;

    @InjectMocks
    private PacienteRepository pacienteRepository;

    private Paciente paciente;
    private PacienteEntity pacienteEntity;

    @BeforeEach
    void setUp() {
        paciente = new Paciente("12345678", "John", "Doe", "Some OS", "john.doe@example.com", "123456789");
        pacienteEntity = new PacienteEntity();
        pacienteEntity.setDni("12345678");
        pacienteEntity.setNombre("John");
        pacienteEntity.setApellido("Doe");
        pacienteEntity.setObraSocial("Some OS");
        pacienteEntity.setEmail("john.doe@example.com");
        pacienteEntity.setTelefono("123456789");
    }

    @Test
    @DisplayName("Debería devolver todos los pacientes")
    void getAll() {
        when(jpaPacienteRepository.findAll()).thenReturn(List.of(pacienteEntity));
        when(pacienteEntityMapper.toDomain(anyList())).thenReturn(List.of(paciente));

        List<Paciente> result = pacienteRepository.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(jpaPacienteRepository, times(1)).findAll();
        verify(pacienteEntityMapper, times(1)).toDomain(anyList());
    }

    @Test
    @DisplayName("Debería devolver un paciente por DNI si existe")
    void getByDniExists() {
        when(jpaPacienteRepository.findByDni("12345678")).thenReturn(Optional.of(pacienteEntity));
        when(pacienteEntityMapper.toDomain(pacienteEntity)).thenReturn(paciente);

        Optional<Paciente> result = pacienteRepository.getByDni("12345678");

        assertTrue(result.isPresent());
        assertEquals(paciente, result.get());
        verify(jpaPacienteRepository, times(1)).findByDni("12345678");
    }

    @Test
    @DisplayName("Debería devolver un Optional vacío si el paciente con el DNI dado no existe")
    void getByDniNoExists() {
        when(jpaPacienteRepository.findByDni("12345678")).thenReturn(Optional.empty());

        Optional<Paciente> result = pacienteRepository.getByDni("12345678");

        assertFalse(result.isPresent());
        verify(jpaPacienteRepository, times(1)).findByDni("12345678");
    }

    @Test
    @DisplayName("Debería devolver una lista de pacientes por nombre")
    void getByNombreContainingIgnoreCase() {
        when(jpaPacienteRepository.findByNombreContainingIgnoreCase("John")).thenReturn(List.of(pacienteEntity));
        when(pacienteEntityMapper.toDomain(anyList())).thenReturn(List.of(paciente));

        List<Paciente> result = pacienteRepository.getByNombreContainingIgnoreCase("John");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(jpaPacienteRepository, times(1)).findByNombreContainingIgnoreCase("John");
    }

    @Test
    @DisplayName("Debería guardar un paciente")
    void save() {
        when(pacienteEntityMapper.toEntity(paciente)).thenReturn(pacienteEntity);

        Paciente result = pacienteRepository.save(paciente);

        assertEquals(paciente, result);
        verify(jpaPacienteRepository, times(1)).save(pacienteEntity);
    }

    @Test
    @DisplayName("Debería eliminar un paciente")
    void delete() {
        when(pacienteEntityMapper.toEntity(paciente)).thenReturn(pacienteEntity);

        pacienteRepository.delete(paciente);

        verify(jpaPacienteRepository, times(1)).delete(pacienteEntity);
    }

    @Test
    @DisplayName("Debería devolver un paciente por nombre con SP si existe")
    void getByNombreExists() {
        when(jpaPacienteRepository.findByNombreSP("John")).thenReturn(Optional.of(pacienteEntity));
        when(pacienteEntityMapper.toDomain(pacienteEntity)).thenReturn(paciente);

        Optional<Paciente> result = pacienteRepository.getByNombre("John");

        assertTrue(result.isPresent());
        assertEquals(paciente, result.get());
        verify(jpaPacienteRepository, times(1)).findByNombreSP("John");
    }

    @Test
    @DisplayName("Debería devolver un Optional vacío si el paciente con el nombre dado no existe con SP")
    void getByNombreNoExists() {
        when(jpaPacienteRepository.findByNombreSP("John")).thenReturn(Optional.empty());

        Optional<Paciente> result = pacienteRepository.getByNombre("John");

        assertFalse(result.isPresent());
        verify(jpaPacienteRepository, times(1)).findByNombreSP("John");
    }

    @Test
    @DisplayName("Debería devolver una lista de pacientes por obra social con SP")
    void getByObraSocial() {
        when(jpaPacienteRepository.findByObraSocialSP("Some OS", 10, 0)).thenReturn(List.of(pacienteEntity));
        when(pacienteEntityMapper.toDomain(anyList())).thenReturn(List.of(paciente));

        List<Paciente> result = pacienteRepository.getByObraSocial("Some OS", 10, 0);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(jpaPacienteRepository, times(1)).findByObraSocialSP("Some OS", 10, 0);
    }

    @Test
    @DisplayName("Debería devolver verdadero si el paciente con el DNI dado existe")
    void existsByDni() {
        when(jpaPacienteRepository.existsByDni("12345678")).thenReturn(true);

        boolean result = pacienteRepository.existsByDni("12345678");

        assertTrue(result);
        verify(jpaPacienteRepository, times(1)).existsByDni("12345678");
    }

    @Test
    @DisplayName("Debería devolver falso si el paciente con el DNI dado no existe")
    void notExistsByDni() {
        when(jpaPacienteRepository.existsByDni("12345678")).thenReturn(false);

        boolean result = pacienteRepository.existsByDni("12345678");

        assertFalse(result);
        verify(jpaPacienteRepository, times(1)).existsByDni("12345678");
    }
}
