package com.swissmedical.patients.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.swissmedical.patients.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByDni(String dni);

    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

    @Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
    Optional<Patient> findByDniSP(String dni);

    @Query(value = "CALL buscar_pacientes_por_nombre(:firstName)", nativeQuery = true)
    List<Patient> findByFirstNameSP(String firstName);

    @Query(value = "CALL buscar_pacientes_por_obra_social_paginado(:socialSecurity, :limit, :offset)", nativeQuery =
            true)
    List<Patient> findBySocialSecuritySP(String socialSecurity, int limit, int offset);

}
