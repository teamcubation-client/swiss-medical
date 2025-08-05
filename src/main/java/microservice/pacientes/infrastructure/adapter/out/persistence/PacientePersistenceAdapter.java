package microservice.pacientes.infrastructure.adapter.out.persistence;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOutRead;
import microservice.pacientes.application.domain.port.out.PacientePortOutWrite;
import microservice.pacientes.shared.PacienteNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PacientePersistenceAdapter implements PacientePortOutRead, PacientePortOutWrite {
    private final PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> findByNombreContainingIgnoreCase(String nombre) {
        List<PacienteEntity> pacienteEntityList = pacienteRepository.findByNombreContainingIgnoreCase(nombre);
        return pacienteEntityList.stream()
                .map(PacienteMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Paciente> buscarByDni(String dni) {
        return pacienteRepository.buscarByDni(dni)
                .map(PacienteMapper::toModel);
    }

    @Override
    public List<Paciente> buscarByNombre(String nombre) {
        return pacienteRepository.buscarByNombre(nombre).stream()
                .map(PacienteMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset) {
        return pacienteRepository.buscarPorObraSocialPaginado(obraSocial, limit, offset).stream()
                .map(PacienteMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Paciente> findById(Long id) {
        return pacienteRepository.findById(id)
                .map(PacienteMapper::toModel);
    }

    @Override
    public Paciente save(Paciente paciente) {
        PacienteEntity entity = PacienteMapper.toEntity(paciente);
        PacienteEntity saved = pacienteRepository.save(entity);
        return PacienteMapper.toModel(saved);
    }

    @Override
    public Paciente update(Paciente paciente) {
        PacienteEntity entity = pacienteRepository.findById(paciente.getId())
                .orElseThrow(() -> PacienteNotFoundException.porId(paciente.getId()));

        if (paciente.getNombre() != null) {
            entity.setNombre(paciente.getNombre());
        }
        if (paciente.getApellido() != null) {
            entity.setApellido(paciente.getApellido());
        }
        if (paciente.getDni() != null) {
            entity.setDni(paciente.getDni());
        }
        if (paciente.getObraSocial() != null) {
            entity.setObraSocial(paciente.getObraSocial());
        }
        if (paciente.getEmail() != null) {
            entity.setEmail(paciente.getEmail());
        }
        if (paciente.getTelefono() != null) {
            entity.setTelefono(paciente.getTelefono());
        }
        if (paciente.getTipoPlanObraSocial() != null) {
            entity.setTipoPlanObraSocial(paciente.getTipoPlanObraSocial());
        }
        if (paciente.getFechaAlta() != null) {
            entity.setFechaAlta(paciente.getFechaAlta());
        }
        entity.setEstado(paciente.isEstado());

        return PacienteMapper.toModel(entity);
    }

    @Override
    public void deleteById(long id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    public List<Paciente> findAll() {
        return pacienteRepository.findAll().stream()
                .map(PacienteMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Paciente> findByEstado(Boolean estado) {
        return pacienteRepository.findByEstado(estado).stream()
                .map(PacienteMapper::toModel)
                .collect(Collectors.toList());
    }
}
