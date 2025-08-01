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
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(PatientPersistenceAdapter.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PatientPersistenceAdapterIntegrationTest {

    @Autowired
    private PatientRepositoryJpa patientRepository;

    private static final String EXISTING_DNI = "12345678";
    private static final String FIRST_NAME_SEARCH_FRAGMENT = "ju";
    private static final String HEALTH_INSURANCE_SWISS = "SWISS";
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_OFFSET = 0;

    @BeforeEach
    void setUp() {
        PatientEntity testPatient = PatientEntity.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .dni(EXISTING_DNI)
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
    void shouldReturnPatient_whenFindByDniExists() {
        Optional<PatientEntity> result = patientRepository.findByDni(EXISTING_DNI);

        assertThat(result).isPresent();
        assertThat(result.get().getDni()).isEqualTo(EXISTING_DNI);
    }

    @Test
    void shouldReturnPatients_whenFindByFirstNameContainsIgnoreCase() {
        Deque<PatientEntity> expected = new LinkedList<>(patientRepository.findByFirstNameContainingIgnoreCase(FIRST_NAME_SEARCH_FRAGMENT));

        assertThat(expected).isNotEmpty();
        assertThat(expected.getFirst().getFirstName()).containsIgnoringCase(FIRST_NAME_SEARCH_FRAGMENT);
    }

    @Test
    void shouldReturnPaginatedPatients_whenFindByHealthInsuranceWithPagination() {
        Deque<PatientEntity> expected = new LinkedList<>(patientRepository.findByHealthInsurancePaginated(
                HEALTH_INSURANCE_SWISS,
                PAGE_SIZE,
                PAGE_OFFSET));

        assertThat(expected).isNotEmpty();
        assertThat(expected.getFirst().getHealthInsurance()).isEqualTo(HEALTH_INSURANCE_SWISS);
    }
}
