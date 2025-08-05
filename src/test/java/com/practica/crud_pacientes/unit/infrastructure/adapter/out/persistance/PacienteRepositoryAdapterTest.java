package com.practica.crud_pacientes.unit.infrastructure.adapter.out.persistance;

import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
import com.practica.crud_pacientes.infrastructure.adapter.out.entity.PacienteEntity;
import com.practica.crud_pacientes.infrastructure.adapter.out.mapper.PacientePersistanceMapper;
import com.practica.crud_pacientes.infrastructure.adapter.out.persistence.PacienteJpaRepository;
import com.practica.crud_pacientes.infrastructure.adapter.out.persistence.PacienteRepositoryAdapter;
import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.practica.crud_pacientes.utils.PacienteTestFactory.buildDomain;
import static com.practica.crud_pacientes.utils.TestConstants.LIMITE;
import static com.practica.crud_pacientes.utils.TestConstants.OBRA_SOCIAL;
import static com.practica.crud_pacientes.utils.TestConstants.NOMBRE;
import static com.practica.crud_pacientes.utils.TestConstants.OFF;
import static com.practica.crud_pacientes.utils.TestConstants.DNI;
import static com.practica.crud_pacientes.utils.TestConstants.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PacienteRepositoryAdapterTest {
    @Mock
    private PacienteJpaRepository jpaRepository;
    @Mock
    private PacientePersistanceMapper mapper;
    @Mock
    private PacienteLoggerPort loggerPort;
    @InjectMocks
    private PacienteRepositoryAdapter pacienteRepositoryAdapter;
    private Paciente paciente;
    private PacienteEntity pacienteEntity;
    private PacienteEntity entity1;
    private PacienteEntity entity2;
    List<PacienteEntity> pacientesEntity;
    Paciente paciente1;
    Paciente paciente2;

    @BeforeEach
    void setUp() {
        this.paciente = buildDomain();

        this.pacienteEntity = new PacienteEntity();

        entity1 = new PacienteEntity();
        entity2 = new PacienteEntity();
        pacientesEntity = List.of(entity1, entity2);

        paciente1 = new Paciente();
        paciente2 = new Paciente();
    }

    @Test
    @DisplayName("Should save paciente when valid domain paciente is provided")
    void shouldSavePaciente_whenValidDomainPacienteProvided() {
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
    @DisplayName("Should return all pacientes when findAll is called")
    void shouldReturnAllPacientes_whenFindAllIsCalled() {
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
    @DisplayName("Should return paciente when ID exists")
    void shouldReturnPaciente_whenIdExists() {
        when(jpaRepository.findById(pacienteEntity.getId())).thenReturn(Optional.ofNullable(pacienteEntity));
        when(mapper.entityToDomain(pacienteEntity)).thenReturn(paciente);

        Paciente foundPaciente = pacienteRepositoryAdapter.findById(pacienteEntity.getId());

        assertEquals(foundPaciente, paciente);
        verify(jpaRepository, times(1)).findById(pacienteEntity.getId());
        verify(mapper, times(1)).entityToDomain(pacienteEntity);
    }

    @Test
    @DisplayName("Should throw PacienteNoEncontradoException when ID does not exist")
    void shouldThrowPacienteNoEncontradoException_whenIdDoesNotExist() {
        when(jpaRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(PacienteNoEncontradoException.class, () ->{
            pacienteRepositoryAdapter.findById(ID);
        });
        verify(jpaRepository, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Should return paciente when DNI exists")
    void shouldReturnPaciente_whenDniExists() {
        when(jpaRepository.getByDni(pacienteEntity.getDni())).thenReturn(pacienteEntity);
        when(mapper.entityToDomain(pacienteEntity)).thenReturn(paciente);

        Paciente foundPaciente = pacienteRepositoryAdapter.getPacienteByDni(pacienteEntity.getDni());

        assertEquals(paciente, foundPaciente);
        verify(jpaRepository, times(1)).getByDni(pacienteEntity.getDni());
        verify(mapper, times(1)).entityToDomain(pacienteEntity);
    }

    @Test
    @DisplayName("Should return null when DNI does not exist")
    void shouldReturnNull_whenDniDoesNotExist() {
        when(jpaRepository.getByDni(DNI)).thenReturn(null);
        when(mapper.entityToDomain(null)).thenReturn(null);

        Paciente foundPaciente = pacienteRepositoryAdapter.getPacienteByDni(DNI);

        assertNull(foundPaciente);
        verify(jpaRepository, times(1)).getByDni(DNI);
        verify(mapper, times(1)).entityToDomain(null);
    }

    @Test
    @DisplayName("Should return pacientes list when nombre matches")
    void shouldReturnPacientesList_whenNombreMatches() {
        when(jpaRepository.getPacientesByNombre(NOMBRE.toLowerCase())).thenReturn(pacientesEntity);
        when(mapper.entityToDomain(entity1)).thenReturn(paciente1);
        when(mapper.entityToDomain(entity2)).thenReturn(paciente2);

        List<Paciente> pacientes = pacienteRepositoryAdapter.getPacientesByNombre(NOMBRE);

        assertEquals(2, pacientes.size());
        verify(jpaRepository, times(1)).getPacientesByNombre(NOMBRE.toLowerCase());
        verify(mapper, times(1)).entityToDomain(entity1);
        verify(mapper, times(1)).entityToDomain(entity2);
    }

    @Test
    @DisplayName("Should return pacientes list when obra social matches with pagination")
    void shouldReturnPacientesList_whenObraSocialMatchesWithPagination() {
        when(jpaRepository.getPacietesbyObraSocial(OBRA_SOCIAL, LIMITE, OFF)).thenReturn(pacientesEntity);
        when(mapper.entityToDomain(entity1)).thenReturn(paciente1);
        when(mapper.entityToDomain(entity2)).thenReturn(paciente2);

        List<Paciente> pacientes = pacienteRepositoryAdapter.getPacientesByObraSocial(OBRA_SOCIAL, LIMITE, OFF);

        assertEquals(2, pacientes.size());
        verify(jpaRepository, times(1)).getPacietesbyObraSocial(OBRA_SOCIAL, LIMITE, OFF);
        verify(mapper, times(1)).entityToDomain(entity1);
        verify(mapper, times(1)).entityToDomain(entity2);
    }

    @Test
    @DisplayName("Should return true when paciente exists by ID")
    void shouldReturnTrue_whenPacienteExistsById() {
        when(jpaRepository.existsById(ID)).thenReturn(true);

        boolean exists = pacienteRepositoryAdapter.existsById(ID);

        assertTrue(exists);
        verify(jpaRepository, times(1)).existsById(ID);
    }

    @Test
    @DisplayName("Should return false when paciente does not exist by ID")
    void shouldReturnFalse_whenPacienteDoesNotExistById() {
        when(jpaRepository.existsById(ID)).thenReturn(false);

        boolean exists = pacienteRepositoryAdapter.existsById(ID);

        assertFalse(exists);
        verify(jpaRepository, times(1)).existsById(ID);
    }

    @Test
    @DisplayName("Should delete paciente when deleteById is called")
    void shouldDeletePaciente_whenDeleteByIdIsCalled() {
        pacienteRepositoryAdapter.deleteById(ID);

        verify(jpaRepository, times(1)).deleteById(ID);
    }

}
