package com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.repository;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    @Mock
    private IPatientRepository mockedJpaRepository;

    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        this.patientRepository = new PatientRepository(jpaRepository);
    }

    static class PatientBuilder {
        private Long id = 1L;
        private String name = "Fabian";
        private String lastName = "Gonzáles";
        private String dni = "35784627";
        private String insurance = null;
        private String email = null;
        private String phone = null;

        PatientRepositoryIT.PatientBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        PatientRepositoryIT.PatientBuilder withName(String name) {
            this.name = name;
            return this;
        }

        PatientRepositoryIT.PatientBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        PatientRepositoryIT.PatientBuilder withDni(String dni) {
            this.dni = dni;
            return this;
        }

        PatientRepositoryIT.PatientBuilder withInsurance() {
            this.insurance = "Swiss Medical";
            return this;
        }

        PatientRepositoryIT.PatientBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        PatientRepositoryIT.PatientBuilder withPhone() {
            this.phone = "+5491199867532";
            return this;
        }

        Patient build() {
            Patient p = new Patient();
            p.setId(id);
            p.setName(name);
            p.setLastName(lastName);
            p.setDni(dni);
            p.setHealthInsuranceProvider(insurance);
            p.setEmail(email);
            p.setPhoneNumber(phone);
            return p;
        }
    }

    //TEST GUARDAR PACIENTE
    @Test
    void savePatientWithRequiredFields_ShouldSavePatientSuccessfully() {
        Patient patient = new PatientBuilder().build();
        Patient saved = this.patientRepository.save(patient);

        assertNotNull(saved.getId());
        assertEquals(patient.getName(), saved.getName());
        assertEquals(patient.getLastName(), saved.getLastName());
        assertEquals(patient.getDni(), saved.getDni());
    }

    @Test
    void savePatientWithAllFields_ShouldSavePatientSuccessfully() {
        Patient patient = new PatientBuilder().withInsurance().withEmail("mail@example.com").withPhone().build();
        Patient saved = this.patientRepository.save(patient);

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
        Patient patient = new PatientBuilder()
                .withName(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            patientRepository.save(patient);
        });
    }

    @Test
    void savePatientWithNullLastName_ShouldThrowException() {
        Patient patient = new PatientBuilder()
                .withLastName(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            patientRepository.save(patient);
        });
    }

    @Test
    void savePatientWithNullDni_ShouldThrowException() {
        Patient patient = new PatientBuilder()
                .withDni(null)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            patientRepository.save(patient);
        });
    }

    @Test
    void saveTwoPatientsWithSameNameAndDifferentDni_ShouldSaveBothSuccessfully() {
        Patient patient1 = new PatientBuilder().build();

        Patient patient2 = new PatientBuilder().withDni("35778654").build();

        Patient saved1 = this.patientRepository.save(patient1);
        Patient saved2 = this.patientRepository.save(patient2);

        assertNotNull(saved1.getId());
        assertNotNull(saved2.getId());
        assertNotEquals(saved1.getId(), saved2.getId());
        assertNotEquals(saved1.getDni(), saved2.getDni());
    }

    @Test
    void savePatientWithDuplicateDni_ShouldThrowDataIntegrityViolationException() {
        Patient patient1 = new PatientBuilder().build();
        String dniInUse = patient1.getDni();
        this.patientRepository.save(patient1);

        Patient patient2 = new PatientBuilder()
                .withName("Otro")
                .withLastName("Paciente")
                .withDni(dniInUse)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            this.patientRepository.save(patient2);
        });
    }

    @Test
    void savePatientWithVeryLongFields_ShouldThrowException() {
        String veryLongName = "a".repeat(110);
        Patient patient = new PatientBuilder()
                .withName(veryLongName)
                .withLastName(veryLongName)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            this.patientRepository.save(patient);
        });
    }

    @Test
    void savePatientWithEmptyButNotNullFields_ShouldSaveSuccessfully() {
        Patient patient = new PatientBuilder()
                .withName("")
                .withLastName("")
                .withDni("")
                .build();

        Patient saved = this.patientRepository.save(patient);

        assertNotNull(saved.getId());
        assertEquals("", saved.getName());
        assertEquals("", saved.getLastName());
    }

    //TEST OBTENER PACIENTES
    @Test
    void findAllWithNullParams_ShouldReturnAllPatients() {
        Patient patient = new PatientBuilder().build();
        Patient patient2 = new PatientBuilder().withName("Test").withDni("87654321").build();
        this.patientRepository.save(patient);
        this.patientRepository.save(patient2);
        List<Patient> patients = this.patientRepository.findAll(null, null);

        assertEquals(2, patients.size());
    }

    @Test
    void findAllWithNullParams_ShouldReturnEmptyList() {
        List<Patient> patients = patientRepository.findAll(null, null);

        assertTrue(patients.isEmpty());
    }

    @Test
    void findAllWithPartialName_ShouldReturnMatchingPatients() {
        Patient patient = new PatientBuilder().withName("Joel").build();
        Patient patient2 = new PatientBuilder().withName("Jonathan").withDni("12345678").build();
        Patient patient3 = new PatientBuilder().withName("María").withDni("23456789").build();
        this.patientRepository.save(patient);
        this.patientRepository.save(patient2);
        this.patientRepository.save(patient3);
        List<Patient> patients = patientRepository.findAll(null, "Jo");

        assertEquals(2, patients.size());
    }

    @Test
    void findAllWithNameNotMatching_ShouldReturnEmptyList() {
        Patient patient = new PatientBuilder().build();
        this.patientRepository.save(patient);

        List<Patient> patients = patientRepository.findAll(null, "Test");

        assertTrue(patients.isEmpty());
    }

    @Test
    void findAllWithExactDni_ShouldReturnSinglePatient() {
        Patient patient = new PatientBuilder().build();
        Patient patient2 = new PatientBuilder().withDni("47857635").build();
        this.patientRepository.save(patient);
        this.patientRepository.save(patient2);

        List<Patient> patients = patientRepository.findAll(patient.getDni(), null);

        assertEquals(1, patients.size());
        assertEquals(patient.getName(), patients.get(0).getName());
    }

    @Test
    void findAllWithNonMatchingDni_ShouldReturnEmptyList() {
        Patient patient = new PatientBuilder().build();
        this.patientRepository.save(patient);
        List<Patient> patients = patientRepository.findAll("99999999", null);

        assertTrue(patients.isEmpty());
    }

    @Test
    void findAllWithMatchingDniAndName_ShouldReturnPatient() {
        Patient patient = new PatientBuilder().build();
        String dni = patient.getDni();
        String name = patient.getName();
        this.patientRepository.save(patient);

        List<Patient> patients = patientRepository.findAll(dni, name);

        assertEquals(1, patients.size());
    }

    @Test
    void findAllWithDniMatchingButNotName_ShouldReturnEmptyList() {
        Patient patient = new PatientBuilder().build();
        String matchingDni = patient.getDni();
        this.patientRepository.save(patient);
        List<Patient> patients = patientRepository.findAll(matchingDni, "Test");

        assertTrue(patients.isEmpty());
    }

    @Test
    void findAllWithDniNotMatchingButNameMatching_ShouldReturnEmptyList() {
        Patient patient = new PatientBuilder().build();
        String matchingName = patient.getName();
        this.patientRepository.save(patient);
        List<Patient> patients = patientRepository.findAll("99999999", matchingName);

        assertTrue(patients.isEmpty());
    }

    //TEST OBTENER PACIENTE POR ID
    @Test
    void findByExistentId_ShouldReturnPatient() {
        Patient patient = new PatientBuilder().build();

        Patient saved = this.patientRepository.save(patient);
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

    //TEST ACTUALIZAR PACIENTE POR ID
    @Test
    void updateByExistentId_ShouldUpdatePatient() {
        Patient patient = patientRepository.save(
                new PatientBuilder().withEmail("original@email.com").build()
        );
        patient.setName("Modificado");
        patient.setEmail("nuevo@email.com");

        Patient updated = patientRepository.updateById(patient);

        assertEquals("Modificado", updated.getName());
        assertEquals("nuevo@email.com", updated.getEmail());
    }

    @Test
    void updateByNonExistentId_ShouldInsertNewPatient() {
        Patient newPatient = new PatientBuilder().build();

        Patient saved = patientRepository.updateById(newPatient);

        assertNotNull(saved.getId());
        assertEquals(newPatient.getName(), saved.getName());
    }

    @Test
    void updateByIdWithMissingRequiredFields_ShouldThrowException() {
        Patient incomplete = new PatientBuilder().withDni(null).build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            patientRepository.updateById(incomplete);
        });
    }

    //TEST BORRAR PACIENTE POR ID
    @Test
    void deleteByExistentId_ShouldRemovePatient() {
        Patient patient = new PatientBuilder().build();

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
