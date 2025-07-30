package com.tq.pacientes.integration;

import com.tq.pacientes.infrastructure.adapter.out.persistence.PatientEntity;
import com.tq.pacientes.infrastructure.adapter.out.persistence.PatientPersistenceAdapter;
import com.tq.pacientes.infrastructure.adapter.out.persistence.PatientRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(PatientPersistenceAdapter.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

class PatientPersistenceAdapterIntegrationTest {

    @Autowired
    private PatientRepositoryJpa patientRepository;

    @BeforeEach
    void setUp() {
        PatientEntity testPatient = PatientEntity.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .dni("12345678")
                .birthDate(LocalDate.of(1990, 1, 1))
                .healthInsurance("SWISS")
                .healthPlan("PREMIUM")
                .address("Calle Test 123")
                .phoneNumber("1234567890")
                .email("juan@test.com")
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        patientRepository.save(testPatient);
    }

    @Test
    void findByDni_ShouldReturnPatient() {
        Optional<PatientEntity> result = patientRepository.findByDni("12345678");

        assertThat(result).isPresent();
        assertThat(result.get().getDni()).isEqualTo("12345678");
    }

    @Test
    void findByFirstNameContainingIgnoreCase_ShouldReturnMatchingPatients() {
        List<PatientEntity> results = patientRepository.findByFirstNameContainingIgnoreCase("ju");

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getFirstName()).containsIgnoringCase("ju");
    }

    @Test
    void findByHealthInsurancePaginated_ShouldReturnPaginatedResults() {
        List<PatientEntity> results = patientRepository.findByHealthInsurancePaginated("SWISS", 10, 0);

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getHealthInsurance()).isEqualTo("SWISS");
    }
}
