package com.tq.pacientes.repositories;

import com.tq.pacientes.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByDni(String dni);

    List<Patient> findByFirstNameContainingIgnoreCase(String firstName);
}