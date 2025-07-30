package com.practica.crud_pacientes.unit.infrastructure.adapter.out.persistance;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.infrastructure.adapter.out.entity.PacienteEntity;
import com.practica.crud_pacientes.infrastructure.adapter.out.mapper.PacientePersistanceMapper;
import com.practica.crud_pacientes.infrastructure.adapter.out.persistence.PacienteJpaRepository;
import com.practica.crud_pacientes.infrastructure.adapter.out.persistence.PacienteRepositoryAdapter;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.buildDomain;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteRepositoryAdapterTest {
    @Mock
    private PacienteJpaRepository jpaRepository;
    @Mock
    private PacientePersistanceMapper mapper;
    @InjectMocks
    private PacienteRepositoryAdapter pacienteRepositoryAdapter;
    private Paciente paciente;
    private PacienteEntity pacienteEntity;

    @BeforeEach
    void setUp() {
        this.paciente = buildDomain();

        this.pacienteEntity = new PacienteEntity();
    }

    @Test
    void shouldSavePaciente() {
        when(mapper.domainToEntity(paciente)).thenReturn(pacienteEntity);
        when(jpaRepository.save(pacienteEntity)).thenReturn(pacienteEntity);
        when(mapper.entityToDomain(pacienteEntity)).thenReturn(paciente);

        Paciente savedPaciente = pacienteRepositoryAdapter.save(paciente);

        assertEquals(savedPaciente, paciente);
        verify(jpaRepository, times(1)).save(pacienteEntity);
        verify(mapper, times(1)).domainToEntity(paciente);
        verify(mapper, times(1)).entityToDomain(pacienteEntity);
    }

    @Test
    void shouldFindAllPacientes() {
        List<PacienteEntity> pacientesEntity = new ArrayList<>();
        PacienteEntity entity1 = new PacienteEntity();
        PacienteEntity entity2 = new PacienteEntity();

        pacientesEntity.add(entity1);
        pacientesEntity.add(entity2);

        Paciente paciente1 = new Paciente();
        Paciente paciente2 = new Paciente();

        when(jpaRepository.findAll()).thenReturn(pacientesEntity);
        when(mapper.entityToDomain(entity1)).thenReturn(paciente1);
        when(mapper.entityToDomain(entity2)).thenReturn(paciente2);

        List<Paciente> pacientes = pacienteRepositoryAdapter.findAll();

        assertEquals(2, pacientes.size());
        verify(jpaRepository, times(1)).findAll();
        verify(mapper, times(1)).entityToDomain(entity1);
        verify(mapper, times(1)).entityToDomain(entity2);
    }

    @Test
    void shouldFindPacienteById() {
        when(jpaRepository.findById(pacienteEntity.getId())).thenReturn(Optional.ofNullable(pacienteEntity));
        when(mapper.entityToDomain(pacienteEntity)).thenReturn(paciente);

        Paciente foundPaciente = pacienteRepositoryAdapter.findById(pacienteEntity.getId());

        assertEquals(foundPaciente, paciente);
        verify(jpaRepository, times(1)).findById(pacienteEntity.getId());
        verify(mapper, times(1)).entityToDomain(pacienteEntity);
    }

    @Test
    void shouldThrowExceptionWhenFindPacienteByid() {
        when(jpaRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(PacienteNoEncontradoException.class, () ->{
            pacienteRepositoryAdapter.findById(100);
        });
        verify(jpaRepository, times(1)).findById(100);
    }

    @Test
    void shouldFindPacienteByDni() {
        when(jpaRepository.getByDni(pacienteEntity.getDni())).thenReturn(pacienteEntity);
        when(mapper.entityToDomain(pacienteEntity)).thenReturn(paciente);

        Paciente foundPaciente = pacienteRepositoryAdapter.getPacienteByDni(pacienteEntity.getDni());

        assertEquals(paciente, foundPaciente);
        verify(jpaRepository, times(1)).getByDni(pacienteEntity.getDni());
        verify(mapper, times(1)).entityToDomain(pacienteEntity);
    }

    @Test
    void shouldReturnNullWhenFindPacienteByDni() {
        String dni = "12345678";
        when(jpaRepository.getByDni(dni)).thenReturn(null);
        when(mapper.entityToDomain(null)).thenReturn(null);

        Paciente foundPaciente = pacienteRepositoryAdapter.getPacienteByDni(dni);

        assertNull(foundPaciente);
        verify(jpaRepository, times(1)).getByDni(dni);
        verify(mapper, times(1)).entityToDomain(null);
    }

    @Test
    void shouldGetPacientesByNombre() {
        String nombre = "John";
        List<PacienteEntity> pacientesEntity = new ArrayList<>();
        PacienteEntity pacienteEntity1 = new PacienteEntity();
        PacienteEntity pacienteEntity2 = new PacienteEntity();
        pacientesEntity.add(pacienteEntity1);
        pacientesEntity.add(pacienteEntity2);

        Paciente paciente1 = new Paciente();
        Paciente paciente2 = new Paciente();

        when(jpaRepository.getPacientesByNombre(nombre.toLowerCase())).thenReturn(pacientesEntity);
        when(mapper.entityToDomain(pacienteEntity1)).thenReturn(paciente1);
        when(mapper.entityToDomain(pacienteEntity2)).thenReturn(paciente2);

        List<Paciente> pacientes = pacienteRepositoryAdapter.getPacientesByNombre(nombre);

        assertEquals(2, pacientes.size());
        verify(jpaRepository, times(1)).getPacientesByNombre(nombre.toLowerCase());
        verify(mapper, times(1)).entityToDomain(pacienteEntity1);
        verify(mapper, times(1)).entityToDomain(pacienteEntity2);
    }

    @Test
    void shouldGetPacientesByObraSocial() {
        String obraSocial = "Swiss Medical";
        int limite = 2;
        int off = 0;
        List<PacienteEntity> pacientesEntity = new ArrayList<>();
        PacienteEntity pacienteEntity1 = new PacienteEntity();
        PacienteEntity pacienteEntity2 = new PacienteEntity();
        pacientesEntity.add(pacienteEntity1);
        pacientesEntity.add(pacienteEntity2);

        Paciente paciente1 = new Paciente();
        Paciente paciente2 = new Paciente();

        when(jpaRepository.getPacietesbyObraSocial(obraSocial, limite, off)).thenReturn(pacientesEntity);
        when(mapper.entityToDomain(pacienteEntity1)).thenReturn(paciente1);
        when(mapper.entityToDomain(pacienteEntity2)).thenReturn(paciente2);

        List<Paciente> pacientes = pacienteRepositoryAdapter.getPacientesByObraSocial(obraSocial, limite, off);

        assertEquals(2, pacientes.size());
        verify(jpaRepository, times(1)).getPacietesbyObraSocial(obraSocial, limite, off);
        verify(mapper, times(1)).entityToDomain(pacienteEntity1);
        verify(mapper, times(1)).entityToDomain(pacienteEntity2);
    }

    @Test
    void shouldReturnTrueWhenPacienteExistsById() {
        int id = 1;
        when(jpaRepository.existsById(id)).thenReturn(true);

        boolean exists = pacienteRepositoryAdapter.existsById(id);

        assertTrue(exists);
        verify(jpaRepository, times(1)).existsById(id);
    }

    @Test
    void shouldReturnFalseWhenPacienteDoesNotExistById() {
        int id = 2;
        when(jpaRepository.existsById(id)).thenReturn(false);

        boolean exists = pacienteRepositoryAdapter.existsById(id);

        assertFalse(exists);
        verify(jpaRepository, times(1)).existsById(id);
    }

    @Test
    void shouldDeletePacienteById() {
        int id = 3;

        pacienteRepositoryAdapter.deleteById(id);

        verify(jpaRepository, times(1)).deleteById(id);
    }

}
