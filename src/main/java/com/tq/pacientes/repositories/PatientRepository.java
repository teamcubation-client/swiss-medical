package com.tq.pacientes.repositories;

import com.tq.pacientes.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "CALL find_patient_by_dni(:dni)", nativeQuery = true)
    Optional<Patient> findByDni(@Param("dni") String dni);

    @Query(value = "CALL find_patients_by_first_name(:firstName)", nativeQuery = true)
    List<Patient> findByFirstNameContainingIgnoreCase(@Param("firstName") String firstName);

    @Query(value = "CALL find_patients_by_health_insurance_paginated(:healthInsurance, :limit, :offset)", nativeQuery = true)
    List<Patient> findByHealthInsurancePaginated(@Param("healthInsurance") String healthInsurance, int limit, int offset);

}