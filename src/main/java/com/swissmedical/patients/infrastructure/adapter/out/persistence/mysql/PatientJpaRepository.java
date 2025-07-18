package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql;

import com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {

  List<PatientEntity> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

  boolean existsByDni(String dni);

  boolean existsByEmail(String email);

  @Query(value = "CALL buscar_pacientes_paginado(:limit, :offset)", nativeQuery = true)
  List<PatientEntity> findAll(int limit, int offset);

  @Query(value = "CALL buscar_paciente_por_dni(:dni)", nativeQuery = true)
  Optional<PatientEntity> findByDni(String dni);

  @Query(value = "CALL buscar_pacientes_por_nombre(:firstName)", nativeQuery = true)
  List<PatientEntity> findByFirstName(String firstName);

  @Query(value = "CALL buscar_pacientes_por_obra_social_paginado(:socialSecurity, :limit, :offset)", nativeQuery =
          true)
  List<PatientEntity> findBySocialSecurity(String socialSecurity, int limit, int offset);
}
