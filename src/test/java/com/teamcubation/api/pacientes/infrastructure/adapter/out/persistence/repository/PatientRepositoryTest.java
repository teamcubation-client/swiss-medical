package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.repository;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.entity.PatientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PatientRepositoryTest {
    @Mock
    private IPatientRepository jpaRepositoryMock;

    private PatientRepository patientRepository;

    private PatientEntity patientEntity1;
    private PatientEntity patientEntity2;
    private List<PatientEntity> patientEntityList;

    @BeforeEach
    void setUp() {
        this.patientRepository = new PatientRepository(this.jpaRepositoryMock);

       this.patientEntity1 = new PatientEntity();
       this.patientEntity1.setName("Ana");
       this.patientEntity1.setDni("12345678");

        this.patientEntity2 = new PatientEntity();
        this.patientEntity2.setName("Carlos");
        this.patientEntity2.setDni("87654321");

        this.patientEntityList = List.of(this.patientEntity1, this.patientEntity2);
    }

    //TEST OBTENER PACIENTE POR DNI
    @Test
    void findByExistentDni_ShouldReturnPatient() {
        String dni = this.patientEntity1.getDni();
        when(this.jpaRepositoryMock.findByDni(dni)).thenReturn(Optional.of(this.patientEntity1));

        Optional<Patient> result = this.patientRepository.findByDni(dni);

        assertTrue(result.isPresent());
        assertEquals(dni, result.get().getDni());
        verify(this.jpaRepositoryMock).findByDni(dni);
    }

    @Test
    void findByNonExistentDni_ShouldReturnEmpty() {
        String dni = "99999999";
        when(this.jpaRepositoryMock.findByDni(dni)).thenReturn(Optional.empty());

        Optional<Patient> result = this.patientRepository.findByDni(dni);

        assertTrue(result.isEmpty());
        verify(this.jpaRepositoryMock).findByDni(dni);
    }

    @Test
    void findByNullDni_ShouldReturnEmpty() {
        when(this.jpaRepositoryMock.findByDni(null)).thenReturn(Optional.empty());
        Optional<Patient> result = this.patientRepository.findByDni(null);

        assertTrue(result.isEmpty());
        verify(this.jpaRepositoryMock).findByDni(null);
    }

    //TEST OBTENER PACIENTES POR NOMBRE
    @Test
    void findByExactName_ShouldReturnPatients() {
        String name = this.patientEntity1.getName();
        when(this.jpaRepositoryMock.findByName(name)).thenReturn(List.of(this.patientEntity1));

        List<Patient> result = this.patientRepository.findByName(name);

        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
        verify(this.jpaRepositoryMock).findByName(name);
    }

    @Test
    void findByNonExistentName_ShouldReturnEmptyList() {
        String name = "Inexistente";

        when(this.jpaRepositoryMock.findByName(name)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByName(name);

        assertTrue(result.isEmpty());
        verify(this.jpaRepositoryMock).findByName(name);
    }

    @Test
    void findByNullName_ShouldReturnEmptyList() {
        when(this.jpaRepositoryMock.findByName(null)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByName(null);

        assertTrue(result.isEmpty());
        verify(this.jpaRepositoryMock).findByName(null);
    }

    @Test
    void findByEmptyName_ShouldReturnEmptyList() {
        when(this.jpaRepositoryMock.findByName("")).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByName("");

        assertTrue(result.isEmpty());
        verify(this.jpaRepositoryMock).findByName("");
    }

    @Test
    void findByNameWithDifferentCase_ShouldReturnPatients() {
        String inputName = "carlos";

        when(this.jpaRepositoryMock.findByName(inputName)).thenReturn(List.of(this.patientEntity2));

        List<Patient> result = this.patientRepository.findByName(inputName);

        assertEquals(1, result.size());
        assertEquals("Carlos", result.get(0).getName());
        verify(this.jpaRepositoryMock).findByName(inputName);
    }

    @Test
    void findByPartialName_ShouldReturnMatchingPatients() {
        String input = "Car";

        when(this.jpaRepositoryMock.findByName(input)).thenReturn(List.of(this.patientEntity2));

        List<Patient> result = this.patientRepository.findByName(input);

        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(p -> p.getName().startsWith("Car")));
        verify(this.jpaRepositoryMock).findByName(input);
    }


    // TEST OBTENER PACIENTES POR OBRA SOCIAL
    @Test
    void findByHealthInsuranceProvider_ShouldReturnPatients() {
        String provider = "OSDE";
        int limit = 2, offset = 0;

        when(this.jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                .thenReturn(patientEntityList);

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

        assertEquals(2, result.size());
        assertEquals(this.patientEntity1.getName(), result.get(0).getName());
        assertEquals(this.patientEntity2.getName(), result.get(1).getName());
        verify(this.jpaRepositoryMock).findByHealthInsuranceProvider(provider, limit, offset);
    }


    @Test
    void findByHealthInsuranceProvider_ShouldReturnEmptyList() {
        String provider = "NO_EXISTE";
        int limit = 2, offset = 0;

        when(this.jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

        assertTrue(result.isEmpty());
        verify(this.jpaRepositoryMock).findByHealthInsuranceProvider(provider, limit, offset);
    }

    @Test
    void findByNonExistentHealthInsuranceProvider_ShouldReturnEmptyList() {
        String provider = "NoExiste";
        int limit = 5, offset = 0;

        when(this.jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByHealthInsuranceProviderDifferentCase_ShouldReturnPatients() {
        String input = "osde";
        PatientEntity p = new PatientEntity(); p.setName("Lucía");

        when(this.jpaRepositoryMock.findByHealthInsuranceProvider(input, 1, 0))
                .thenReturn(List.of(p));

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider(input, 1, 0);

        assertEquals(1, result.size());
        assertEquals("Lucía", result.get(0).getName());
    }

    @Test
    void findByNullHealthInsuranceProvider_ShouldReturnEmptyList() {
        when(this.jpaRepositoryMock.findByHealthInsuranceProvider(null, 5, 0))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider(null, 5, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByHealthInsuranceProvider_EmptyProvider_ShouldReturnEmptyList() {
        when(this.jpaRepositoryMock.findByHealthInsuranceProvider("", 5, 0))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider("", 5, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByHealthInsuranceProvider_OffsetBeyondRange_ShouldReturnEmptyList() {
        String provider = "OSDE";
        int limit = 5, offset = 999;

        when(this.jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByHealthInsuranceProvider_LimitZero_ShouldReturnEmptyList() {
        String provider = "OSDE";
        int limit = 0, offset = 0;

        when(this.jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

        assertTrue(result.isEmpty());
    }

}