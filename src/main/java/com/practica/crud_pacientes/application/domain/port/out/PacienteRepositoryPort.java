package com.practica.crud_pacientes.application.domain.port.out;

import com.practica.crud_pacientes.application.domain.model.Paciente;

import java.util.List;

public interface PacienteRepositoryPort {
    Paciente save(Paciente paciente);
    List<Paciente> findAll();
    Paciente findById(int id);
    Boolean existsById(int id);
    void deleteById(int id);
    Paciente getByDni(String dni);
    List<Paciente> getPacientesByNombre(String nombre);
    List<Paciente> getPacietesbyObraSocial(String obraSocial, int limite, int off);
}
