package com.practica.crud_pacientes.service;

import com.practica.crud_pacientes.exceptions.PacienteNoEncontradoException;
import com.practica.crud_pacientes.model.Paciente;

import java.util.List;

public interface IPacienteService {

    List<Paciente> getPacientes();

    Paciente getPacienteById(int id) throws PacienteNoEncontradoException;

    Paciente addPaciente(Paciente pacienteDto);

    Paciente updatePaciente(int id, Paciente pacienteDto) throws PacienteNoEncontradoException;

    void deletePaciente(int id) throws PacienteNoEncontradoException;

    Paciente getByDni(String dni);

    List<Paciente> getPacientesbyName(String nombre);

    List<Paciente> getPacietesbyObraSocial(String obraSocial, int limite, int off);

}
