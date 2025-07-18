package com.practica.crud_pacientes.application.domain.port.in;

import com.practica.crud_pacientes.shared.exceptions.PacienteNoEncontradoException;
import com.practica.crud_pacientes.application.domain.model.Paciente;


import java.util.List;

public interface PacienteUseCase {

    List<Paciente> getPacientes();
    Paciente getPacienteById(int id);
    Paciente addPaciente(Paciente pacienteDto);
    Paciente updatePaciente(int id, Paciente pacienteDto) throws PacienteNoEncontradoException;
    void deletePaciente(int id) throws PacienteNoEncontradoException;
    Paciente getByDni(String dni);
    List<Paciente> getPacientesbyName(String nombre);
    List<Paciente> getPacietesbyObraSocial(String obraSocial, int limite, int off);
}
