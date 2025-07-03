package microservice.pacientes.service;

import microservice.pacientes.dto.PacienteRequestDTO;
import microservice.pacientes.dto.PacienteResponseDTO;
import microservice.pacientes.dto.PacienteUpdateDTO;
import microservice.pacientes.exception.PacienteDuplicadoException;
import microservice.pacientes.exception.PacienteNoEncontradoException;
import microservice.pacientes.model.Paciente;
import microservice.pacientes.repository.PacienteRepository;
import microservice.pacientes.util.PacienteRequestMapper;
import microservice.pacientes.util.PacienteResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public List<PacienteResponseDTO> getPacientes() {
        return PacienteResponseMapper.toDTO(pacienteRepository.findAll());
    }

    @Override
    public PacienteResponseDTO getPacienteByDni(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado"));
        return PacienteResponseMapper.toDTO(paciente);
    }

    @Override
    public PacienteResponseDTO createPaciente(PacienteRequestDTO pacienteRequestDTO) throws PacienteDuplicadoException {
        if (pacienteRepository.existsByDni(pacienteRequestDTO.getDni()))
            throw new PacienteDuplicadoException("Paciente duplicado");

        Paciente paciente = PacienteRequestMapper.toEntity(pacienteRequestDTO);
        return PacienteResponseMapper.toDTO(pacienteRepository.save(paciente));
    }


    @Override
    public PacienteResponseDTO updatePaciente(String dni, PacienteUpdateDTO pacienteUpdateDTO) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado"));

        // mala practica esto? viola O de solid?
        if (pacienteUpdateDTO.getNombre() != null) paciente.setNombre(pacienteUpdateDTO.getNombre());
        if (pacienteUpdateDTO.getApellido() != null) paciente.setApellido(pacienteUpdateDTO.getApellido());
        if (pacienteUpdateDTO.getObraSocial() != null) paciente.setObraSocial(pacienteUpdateDTO.getObraSocial());
        if (pacienteUpdateDTO.getEmail() != null) paciente.setEmail(pacienteUpdateDTO.getEmail());
        if (pacienteUpdateDTO.getTelefono() != null) paciente.setTelefono(pacienteUpdateDTO.getTelefono());

        return PacienteResponseMapper.toDTO(pacienteRepository.save(paciente));
    }

    @Override
    public void deletePaciente(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado"));
        pacienteRepository.delete(paciente);
    }

    @Override
    public List<PacienteResponseDTO> findByNombreContainingIgnoreCase(String nombre) {
        return PacienteResponseMapper.toDTO(pacienteRepository.findByNombreContainingIgnoreCase(nombre));
    }
}
