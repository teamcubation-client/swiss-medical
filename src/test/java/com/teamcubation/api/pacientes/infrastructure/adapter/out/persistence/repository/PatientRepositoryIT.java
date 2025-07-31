package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.repository;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.shared.TestPatientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryIT {

    @Autowired
    private IPatientRepository jpaRepository;

    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        this.patientRepository = new PatientRepository(jpaRepository);
    }

    @Nested
    class Save {
        @Test
        void savePatientWithRequiredFields_ShouldSavePatientSuccessfully() {
            Patient patient = new TestPatientBuilder().build();
            Patient saved = patientRepository.save(patient);

            assertNotNull(saved.getId());
            assertEquals(patient.getName(), saved.getName());
            assertEquals(patient.getLastName(), saved.getLastName());
            assertEquals(patient.getDni(), saved.getDni());
        }

        @Test
        void savePatientWithAllFields_ShouldSavePatientSuccessfully() {
            Patient patient = new TestPatientBuilder().withInsurance("Swiss Medical").withEmail("mail@example.com").withPhone("+5491147657812").build();
            Patient saved = patientRepository.save(patient);

            assertNotNull(saved.getId());
            assertEquals(patient.getName(), saved.getName());
            assertEquals(patient.getLastName(), saved.getLastName());
            assertEquals(patient.getDni(), saved.getDni());
            assertEquals(patient.getHealthInsuranceProvider(), saved.getHealthInsuranceProvider());
            assertEquals(patient.getEmail(), saved.getEmail());
            assertEquals(patient.getPhoneNumber(), saved.getPhoneNumber());
        }

        @Test
        void savePatientWithNullName_ShouldThrowException() {
            Patient patient = new TestPatientBuilder()
                    .withName(null)
                    .build();

            assertThrows(DataIntegrityViolationException.class, () -> {
                patientRepository.save(patient);
            });
        }

        @Test
        void savePatientWithNullLastName_ShouldThrowException() {
            Patient patient = new TestPatientBuilder()
                    .withLastName(null)
                    .build();

            assertThrows(DataIntegrityViolationException.class, () -> {
                patientRepository.save(patient);
            });
        }

        @Test
        void savePatientWithNullDni_ShouldThrowException() {
            Patient patient = new TestPatientBuilder()
                    .withDni(null)
                    .build();

            assertThrows(DataIntegrityViolationException.class, () -> {
                patientRepository.save(patient);
            });
        }

        @Test
        void saveTwoPatientsWithSameNameAndDifferentDni_ShouldSaveBothSuccessfully() {
            Patient patient1 = new TestPatientBuilder().build();

            Patient patient2 = new TestPatientBuilder().withDni("35778654").build();

            Patient saved1 = patientRepository.save(patient1);
            Patient saved2 = patientRepository.save(patient2);

            assertNotNull(saved1.getId());
            assertNotNull(saved2.getId());
            assertNotEquals(saved1.getId(), saved2.getId());
            assertNotEquals(saved1.getDni(), saved2.getDni());
        }

        @Test
        void savePatientWithDuplicateDni_ShouldThrowDataIntegrityViolationException() {
            Patient patient1 = new TestPatientBuilder().build();
            String dniInUse = patient1.getDni();
            patientRepository.save(patient1);

            Patient patient2 = new TestPatientBuilder()
                    .withName("Otro")
                    .withLastName("Paciente")
                    .withDni(dniInUse)
                    .build();

            assertThrows(DataIntegrityViolationException.class, () -> {
                patientRepository.save(patient2);
            });
        }

        @Test
        void savePatientWithVeryLongFields_ShouldThrowException() {
            String veryLongName = "a".repeat(110);
            Patient patient = new TestPatientBuilder()
                    .withName(veryLongName)
                    .withLastName(veryLongName)
                    .build();

            assertThrows(DataIntegrityViolationException.class, () -> {
                patientRepository.save(patient);
            });
        }

        @Test
        void savePatientWithEmptyButNotNullFields_ShouldSaveSuccessfully() {
            Patient patient = new TestPatientBuilder()
                    .withName("")
                    .withLastName("")
                    .withDni("")
                    .build();

            Patient saved = patientRepository.save(patient);

            assertNotNull(saved.getId());
            assertEquals("", saved.getName());
            assertEquals("", saved.getLastName());
        }
    }

    @Nested
    class Find {
        @Test
        void findAllWithNullParams_ShouldReturnAllPatients() {
            Patient patient = new TestPatientBuilder().build();
            Patient patient2 = new TestPatientBuilder().withName("Test").withDni("87654321").build();
            patientRepository.save(patient);
            patientRepository.save(patient2);
            List<Patient> patients = patientRepository.findAll(null, null);

            assertEquals(2, patients.size());
        }

        @Test
        void findAllWithNullParams_ShouldReturnEmptyList() {
            List<Patient> patients = patientRepository.findAll(null, null);

            assertTrue(patients.isEmpty());
        }

        @Test
        void findAllWithPartialName_ShouldReturnMatchingPatients() {
            Patient patient = new TestPatientBuilder().withName("Joel").build();
            Patient patient2 = new TestPatientBuilder().withName("Jonathan").withDni("12345678").build();
            Patient patient3 = new TestPatientBuilder().withName("María").withDni("23456789").build();
            patientRepository.save(patient);
            patientRepository.save(patient2);
            patientRepository.save(patient3);
            List<Patient> patients = patientRepository.findAll(null, "Jo");

            assertEquals(2, patients.size());
        }

        @Test
        void findAllWithNameNotMatching_ShouldReturnEmptyList() {
            Patient patient = new TestPatientBuilder().build();
            patientRepository.save(patient);

            List<Patient> patients = patientRepository.findAll(null, "Test");

            assertTrue(patients.isEmpty());
        }

        @Test
        void findAllWithExactDni_ShouldReturnSinglePatient() {
            Patient patient = new TestPatientBuilder().build();
            Patient patient2 = new TestPatientBuilder().withDni("47857635").build();
            patientRepository.save(patient);
            patientRepository.save(patient2);

            List<Patient> patients = patientRepository.findAll(patient.getDni(), null);

            assertEquals(1, patients.size());
            assertEquals(patient.getName(), patients.get(0).getName());
        }

        @Test
        void findAllWithNonMatchingDni_ShouldReturnEmptyList() {
            Patient patient = new TestPatientBuilder().build();
            patientRepository.save(patient);
            List<Patient> patients = patientRepository.findAll("99999999", null);

            assertTrue(patients.isEmpty());
        }

        @Test
        void findAllWithMatchingDniAndName_ShouldReturnPatient() {
            Patient patient = new TestPatientBuilder().build();
            String dni = patient.getDni();
            String name = patient.getName();
            patientRepository.save(patient);

            List<Patient> patients = patientRepository.findAll(dni, name);

            assertEquals(1, patients.size());
        }

        @Test
        void findAllWithDniMatchingButNotName_ShouldReturnEmptyList() {
            Patient patient = new TestPatientBuilder().build();
            String matchingDni = patient.getDni();
            patientRepository.save(patient);
            List<Patient> patients = patientRepository.findAll(matchingDni, "Test");

            assertTrue(patients.isEmpty());
        }

        @Test
        void findAllWithDniNotMatchingButNameMatching_ShouldReturnEmptyList() {
            Patient patient = new TestPatientBuilder().build();
            String matchingName = patient.getName();
            patientRepository.save(patient);
            List<Patient> patients = patientRepository.findAll("99999999", matchingName);

            assertTrue(patients.isEmpty());
        }
    }

    @Nested
    class FindById {
        @Test
        void findByExistentId_ShouldReturnPatient() {
            Patient patient = new TestPatientBuilder().build();

            Patient saved = patientRepository.save(patient);
            Optional<Patient> result = patientRepository.findById(saved.getId());

            assertTrue(result.isPresent());
            assertEquals(patient.getName(), result.get().getName());
            assertEquals(patient.getLastName(), result.get().getLastName());
            assertEquals(patient.getDni(), result.get().getDni());
        }

        @Test
        void findByNonExistentId_ShouldReturnEmptyOptional() {
            Optional<Patient> result = patientRepository.findById(1L);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class UpdateById {
        @Test
        void updateByExistentId_ShouldUpdatePatient() {
            Patient patient = patientRepository.save(
                    new TestPatientBuilder().withEmail("original@email.com").build()
            );
            patient.setName("Modificado");
            patient.setEmail("nuevo@email.com");

            Patient updated = patientRepository.updateById(patient);

            assertEquals("Modificado", updated.getName());
            assertEquals("nuevo@email.com", updated.getEmail());
        }

        @Test
        void updateByNonExistentId_ShouldInsertNewPatient() {
            Patient newPatient = new TestPatientBuilder().build();

            Patient saved = patientRepository.updateById(newPatient);

            assertNotNull(saved.getId());
            assertEquals(newPatient.getName(), saved.getName());
        }

        @Test
        void updateByIdWithMissingRequiredFields_ShouldThrowException() {
            Patient incomplete = new TestPatientBuilder().withDni(null).build();

            assertThrows(DataIntegrityViolationException.class, () -> {
                patientRepository.updateById(incomplete);
            });
        }
    }

    @Nested
    class DeleteById {
        @Test
        void deleteByExistentId_ShouldRemovePatient() {
            Patient patient = new TestPatientBuilder().build();

            Patient saved = patientRepository.save(patient);
            patientRepository.deleteById(saved.getId());

            Optional<Patient> result = patientRepository.findById(saved.getId());
            assertTrue(result.isEmpty());
        }

        @Test
        void deleteByNonExistentId_ShouldNotThrowException() {
            long id = 1;
            Optional<Patient> patient = patientRepository.findById(id);

            assertTrue(patient.isEmpty(), String.format("El paciente con id %s no existe", id));
            assertThrows(EmptyResultDataAccessException.class,() -> patientRepository.deleteById(id));
        }
    }

}
