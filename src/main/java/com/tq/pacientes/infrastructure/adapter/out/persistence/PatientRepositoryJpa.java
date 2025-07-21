package com.tq.pacientes.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface PatientRepositoryJpa extends JpaRepository<PatientEntity, Long> {
    @Query(value = "CALL find_patient_by_dni(:dni)", nativeQuery = true)
    Optional<PatientEntity> findByDni(@Param("dni") String dni);

    @Query(value = "CALL find_patients_by_first_name(:firstName)", nativeQuery = true)
    List<PatientEntity> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    @Query(value = "CALL find_patients_by_health_insurance_paginated(:healthInsurance, :limit, :offset)", nativeQuery = true)
    List<PatientEntity> findByHealthInsurancePaginated(@Param("healthInsurance") String healthInsurance, @Param("limit") int limit, @Param("offset") int offset);
} 