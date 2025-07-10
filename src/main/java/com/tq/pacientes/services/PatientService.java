package com.tq.pacientes.services;

import com.tq.pacientes.models.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient crearPatient(Patient Patient);
    List<Patient> listarPatients();
    Optional<Patient> buscarPorId(Long id);
    Optional<Patient> buscarPorDni(String dni);
    List<Patient> buscarPorNombre(String nombre);
    Patient actualizar(Long id, Patient Patient);
    void eliminar(Long id);
    void darDeAlta(Long id);
}
