package microservice.pacientes.service;

import microservice.pacientes.dto.PacienteRequestDTO;
import microservice.pacientes.dto.PacienteResponseDTO;
import microservice.pacientes.dto.PacienteUpdateDTO;
import microservice.pacientes.exception.PacienteDuplicadoException;
import microservice.pacientes.exception.PacienteNoEncontradoException;

import java.util.List;

public interface PacienteService {
    List<PacienteResponseDTO> getPacientes();
    PacienteResponseDTO getPacienteByDni(String dni) throws PacienteNoEncontradoException;
    PacienteResponseDTO createPaciente(PacienteRequestDTO pacienteResponseDTO) throws PacienteDuplicadoException;
    PacienteResponseDTO updatePaciente(String dni, PacienteUpdateDTO pacienteUpdateDTO) throws PacienteNoEncontradoException;
    void deletePaciente(String dni) throws PacienteNoEncontradoException;
    List<PacienteResponseDTO> findByNombreContainingIgnoreCase(String nombre);
}
