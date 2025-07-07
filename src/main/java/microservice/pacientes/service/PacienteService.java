package microservice.pacientes.service;

import microservice.pacientes.controller.dto.PacienteRequestDTO;
import microservice.pacientes.controller.dto.PacienteUpdateDTO;
import microservice.pacientes.exception.PacienteDuplicadoException;
import microservice.pacientes.exception.PacienteNoEncontradoException;
import microservice.pacientes.model.Paciente;

import java.util.List;

public interface PacienteService {
    List<Paciente> getAll();
    Paciente getByDni(String dni) throws PacienteNoEncontradoException;
    Paciente create(PacienteRequestDTO pacienteResponseDTO) throws PacienteDuplicadoException;
    Paciente update(String dni, PacienteUpdateDTO pacienteUpdateDTO) throws PacienteNoEncontradoException;
    void delete(String dni) throws PacienteNoEncontradoException;
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);
}
