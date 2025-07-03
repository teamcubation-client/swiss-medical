package com.tq.pacientes.services.servicesImpl;

import com.tq.pacientes.dtos.ActualizarPacienteDTO;
import com.tq.pacientes.exceptions.PacienteDniNoEncontradoException;
import com.tq.pacientes.exceptions.PacienteDuplicadoException;
import com.tq.pacientes.exceptions.PacienteNoEncontradoException;
import com.tq.pacientes.dtos.PacienteDTO;
import com.tq.pacientes.mappers.PacienteMapper;
import com.tq.pacientes.models.Paciente;
import com.tq.pacientes.repositories.PacienteRepository;
import com.tq.pacientes.services.PacienteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PacienteServicesImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper mapper;

    public PacienteServicesImpl(PacienteRepository pacienteRepository, PacienteMapper mapper) {
        this.pacienteRepository = pacienteRepository;
        this.mapper = mapper;
    }

    @Override
    public PacienteDTO crearPaciente(Paciente paciente) {
        pacienteRepository.findByDni(paciente.getDni())
                .ifPresent(p -> { throw new PacienteDuplicadoException(paciente.getDni()); });

        paciente.setFechaCreacion(LocalDateTime.now());
        paciente.setFechaModificacion(LocalDateTime.now());
        Paciente guardado = pacienteRepository.save(paciente);
        return mapper.toDto(guardado);
    }

    @Override
    public List<PacienteDTO> listarPacientes() {
        return pacienteRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PacienteDTO buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni)
                .map(mapper::toDto)
                .orElseThrow(() -> new PacienteDniNoEncontradoException(dni));
    }

    @Override
    public List<PacienteDTO> buscarPorNombre(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PacienteDTO buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
        return mapper.toDto(paciente);
    }

    @Override
    public PacienteDTO actualizar(Long id, ActualizarPacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));

        if (dto.dni() != null && !dto.dni().equals(paciente.getDni())) {
            pacienteRepository.findByDni(dto.dni()).ifPresent(p -> {
                throw new PacienteDuplicadoException(dto.dni());
            });
            paciente.setDni(dto.dni());
        }

        if (dto.nombre() != null) paciente.setNombre(dto.nombre());
        if (dto.apellido() != null) paciente.setApellido(dto.apellido());
        if (dto.obraSocial() != null) paciente.setObraSocial(dto.obraSocial());
        if (dto.email() != null) paciente.setEmail(dto.email());
        if (dto.telefono() != null) paciente.setTelefono(dto.telefono());

        paciente.setFechaModificacion(LocalDateTime.now());
        return mapper.toDto(pacienteRepository.save(paciente));
    }

    @Override
    public void eliminar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));

        paciente.setActivo(false);
        paciente.setFechaModificacion(LocalDateTime.now());
        pacienteRepository.save(paciente);
    }
}
