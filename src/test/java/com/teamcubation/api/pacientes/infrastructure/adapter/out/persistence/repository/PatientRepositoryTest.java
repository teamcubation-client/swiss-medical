package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.repository;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.entity.PatientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @Nested
    class FindByDni {
        @Test
        void findByExistentDni_ShouldReturnPatient() {
            String dni = patientEntity1.getDni();
            when(jpaRepositoryMock.findByDni(dni)).thenReturn(Optional.of(patientEntity1));

            Optional<Patient> result = patientRepository.findByDni(dni);

            assertTrue(result.isPresent());
            assertEquals(dni, result.get().getDni());
            verify(jpaRepositoryMock).findByDni(dni);
        }

        @Test
        void findByNonExistentDni_ShouldReturnEmpty() {
            String dni = "99999999";
            when(jpaRepositoryMock.findByDni(dni)).thenReturn(Optional.empty());

            Optional<Patient> result = patientRepository.findByDni(dni);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByDni(dni);
        }

        @Test
        void findByNullDni_ShouldReturnEmpty() {
            when(jpaRepositoryMock.findByDni(null)).thenReturn(Optional.empty());
            Optional<Patient> result = patientRepository.findByDni(null);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByDni(null);
        }
    }

    @Nested
    class FindByName {
        @Test
        void findByExactName_ShouldReturnPatients() {
            String name = patientEntity1.getName();
            when(jpaRepositoryMock.findByName(name)).thenReturn(List.of(patientEntity1));

            List<Patient> result = patientRepository.findByName(name);

            assertEquals(1, result.size());
            assertEquals(name, result.get(0).getName());
            verify(jpaRepositoryMock).findByName(name);
        }

        @Test
        void findByNonExistentName_ShouldReturnEmptyList() {
            String name = "Inexistente";

            when(jpaRepositoryMock.findByName(name)).thenReturn(Collections.emptyList());

            List<Patient> result = patientRepository.findByName(name);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByName(name);
        }

        @Test
        void findByNullName_ShouldReturnEmptyList() {
            when(jpaRepositoryMock.findByName(null)).thenReturn(Collections.emptyList());

            List<Patient> result = patientRepository.findByName(null);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByName(null);
        }

        @Test
        void findByEmptyName_ShouldReturnEmptyList() {
            when(jpaRepositoryMock.findByName("")).thenReturn(Collections.emptyList());

            List<Patient> result = patientRepository.findByName("");

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByName("");
        }

        @Test
        void findByNameWithDifferentCase_ShouldReturnPatients() {
            String inputName = "carlos";

            when(jpaRepositoryMock.findByName(inputName)).thenReturn(List.of(patientEntity2));

            List<Patient> result = patientRepository.findByName(inputName);

            assertEquals(1, result.size());
            assertEquals("Carlos", result.get(0).getName());
            verify(jpaRepositoryMock).findByName(inputName);
        }

        @Test
        void findByPartialName_ShouldReturnMatchingPatients() {
            String input = "Car";

            when(jpaRepositoryMock.findByName(input)).thenReturn(List.of(patientEntity2));

            List<Patient> result = patientRepository.findByName(input);

            assertEquals(1, result.size());
            assertTrue(result.stream().allMatch(p -> p.getName().startsWith("Car")));
            verify(jpaRepositoryMock).findByName(input);
        }
    }

    @Nested
    class FindByHealthInsuranceProvider {
        @Test
        void findByHealthInsuranceProvider_ShouldReturnPatients() {
            String provider = "OSDE";
            int limit = 2, offset = 0;

            when(jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                    .thenReturn(patientEntityList);

            List<Patient> result = patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

            assertEquals(2, result.size());
            assertEquals(patientEntity1.getName(), result.get(0).getName());
            assertEquals(patientEntity2.getName(), result.get(1).getName());
            verify(jpaRepositoryMock).findByHealthInsuranceProvider(provider, limit, offset);
        }

        @Test
        void findByHealthInsuranceProvider_ShouldReturnEmptyList() {
            String provider = "NO_EXISTE";
            int limit = 2, offset = 0;

            when(jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                    .thenReturn(Collections.emptyList());

            List<Patient> result = patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByHealthInsuranceProvider(provider, limit, offset);
        }

        @ParameterizedTest
        @CsvSource({
                "NoExiste, 5, 0",
                "OSDE, 5, 999",
                "OSDE, 0, 0"
        })
        void findByHealthInsuranceProvider_ShouldReturnEmptyList(String provider, int limit, int offset) {
            when(jpaRepositoryMock.findByHealthInsuranceProvider(provider, limit, offset))
                    .thenReturn(Collections.emptyList());

            List<Patient> result = patientRepository.findByHealthInsuranceProvider(provider, limit, offset);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByHealthInsuranceProvider(provider,limit,offset);
        }

        @Test
        void findByHealthInsuranceProviderDifferentCase_ShouldReturnPatients() {
            String input = "osde";
            PatientEntity p = new PatientEntity(); p.setName("Lucía");

            when(jpaRepositoryMock.findByHealthInsuranceProvider(input, 1, 0))
                    .thenReturn(List.of(p));

            List<Patient> result = patientRepository.findByHealthInsuranceProvider(input, 1, 0);

            assertEquals(1, result.size());
            assertEquals("Lucía", result.get(0).getName());
            verify(jpaRepositoryMock).findByHealthInsuranceProvider(input,1,0);
        }

        @Test
        void findByNullHealthInsuranceProvider_ShouldReturnEmptyList() {
            when(jpaRepositoryMock.findByHealthInsuranceProvider(null, 5, 0))
                    .thenReturn(Collections.emptyList());

            List<Patient> result = patientRepository.findByHealthInsuranceProvider(null, 5, 0);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByHealthInsuranceProvider(null,5,0);
        }

        @Test
        void findByHealthInsuranceProviderWithEmptyProvider_ShouldReturnEmptyList() {
            when(jpaRepositoryMock.findByHealthInsuranceProvider("", 5, 0))
                    .thenReturn(Collections.emptyList());

            List<Patient> result = patientRepository.findByHealthInsuranceProvider("", 5, 0);

            assertTrue(result.isEmpty());
            verify(jpaRepositoryMock).findByHealthInsuranceProvider("",5,0);
        }
    }
}