package com.teamcubation.api.pacientes.repository;

import com.teamcubation.api.pacientes.model.Paciente;

import java.util.List;
import java.util.Optional;

public interface IPacienteRepository {
    Paciente guardar(Paciente paciente);
    Optional<Paciente> buscarPorID(Long id);
    Optional<Paciente> buscarPorDNI(String dni);
    List<Paciente> buscarTodos(String dni, String nombre);
    Paciente actualizarPorID(Long id, Paciente paciente);
    void borrarPorID(Long id);
}
