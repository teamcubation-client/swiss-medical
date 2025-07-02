package swissmedical.service.impl;

import swissmedical.model.Paciente;
import swissmedical.repository.PacienteRepository;
import swissmedical.service.PacienteService;
import java.util.List;
import org.springframework.stereotype.Service;
import swissmedical.exception.PacienteDuplicadoException;
import swissmedical.exception.PacienteNotFoundException;

@Service
public class PacienteServiceImpl implements PacienteService {
    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente crearPaciente(Paciente paciente) {
        Paciente pacienteExistente = pacienteRepository.findByDni(paciente.getDni()).orElse(null);

        if (pacienteExistente != null) {
            throw new PacienteDuplicadoException(paciente.getDni());
        }
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente obtenerPacientePorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);

        if (paciente == null) {
            throw new PacienteNotFoundException(id);
        }

        return paciente;
    }

    @Override
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    public void eliminarPaciente(Long id) {
        boolean existe = pacienteRepository.existsById(id);

        if (!existe) {
            throw new PacienteNotFoundException(id);
        }

        pacienteRepository.deleteById(id);
    }

    @Override
    public Paciente buscarPorDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni).orElse(null);
        return paciente;
    }

    @Override
    public List<Paciente> buscarPorNombreParcial(String nombre) {

        List<Paciente> pacientes = pacienteRepository.findByNombreContainingIgnoreCase(nombre);
        return pacientes;
    }

    @Override
    public Paciente actualizarPaciente(Long id, Paciente paciente) {
        Paciente existente = pacienteRepository.findById(id).orElse(null);

        if (existente == null) {
            throw new PacienteNotFoundException(id);
        }
        if (paciente.getNombre() != null) {
            existente.setNombre(paciente.getNombre());
        }
        if (paciente.getApellido() != null) {
            existente.setApellido(paciente.getApellido());
        }
        if (paciente.getDni() != null) {
            existente.setDni(paciente.getDni());
        }
        if (paciente.getObraSocial() != null) {
            existente.setObraSocial(paciente.getObraSocial());
        }
        if (paciente.getEmail() != null) {
            existente.setEmail(paciente.getEmail());
        }
        if (paciente.getTelefono() != null) {
            existente.setTelefono(paciente.getTelefono());
        }
        return pacienteRepository.save(existente);
    }
} 