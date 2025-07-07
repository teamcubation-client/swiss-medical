package microservice.pacientes.service;

import microservice.pacientes.controller.dto.PacienteRequestDTO;
import microservice.pacientes.controller.dto.PacienteUpdateDTO;
import microservice.pacientes.exception.PacienteDuplicadoException;
import microservice.pacientes.exception.PacienteNoEncontradoException;
import microservice.pacientes.model.Paciente;
import microservice.pacientes.repository.PacienteRepository;
import microservice.pacientes.util.PacienteRequestMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public List<Paciente> getAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente getByDni(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);
        return paciente;
    }

    @Override
    public Paciente create(PacienteRequestDTO pacienteRequestDTO) throws PacienteDuplicadoException {
        if (pacienteRepository.existsByDni(pacienteRequestDTO.getDni()))
            throw new PacienteDuplicadoException();

        Paciente paciente = PacienteRequestMapper.toEntity(pacienteRequestDTO);
        return pacienteRepository.save(paciente);
    }


    @Override
    public Paciente update(String dni, PacienteUpdateDTO pacienteUpdateDTO) throws PacienteNoEncontradoException {
        Optional<Paciente> optionalPaciente = pacienteRepository.findByDni(dni);
        if (optionalPaciente.isEmpty())
            throw new PacienteNoEncontradoException();
        Paciente paciente = optionalPaciente.get();

        if (pacienteUpdateDTO.getNombre() != null) paciente.setNombre(pacienteUpdateDTO.getNombre());
        if (pacienteUpdateDTO.getApellido() != null) paciente.setApellido(pacienteUpdateDTO.getApellido());
        if (pacienteUpdateDTO.getObraSocial() != null) paciente.setObraSocial(pacienteUpdateDTO.getObraSocial());
        if (pacienteUpdateDTO.getEmail() != null) paciente.setEmail(pacienteUpdateDTO.getEmail());
        if (pacienteUpdateDTO.getTelefono() != null) paciente.setTelefono(pacienteUpdateDTO.getTelefono());

        return pacienteRepository.save(paciente);
    }

    @Override
    public void delete(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);
        pacienteRepository.delete(paciente);
    }

    @Override
    public List<Paciente> findByNombreContainingIgnoreCase(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
