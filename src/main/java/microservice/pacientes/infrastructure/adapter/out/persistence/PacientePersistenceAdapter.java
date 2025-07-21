package microservice.pacientes.infrastructure.adapter.out.persistence;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.out.PacientePortOut;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PacientePersistenceAdapter implements PacientePortOut {
    private final PacienteRepository pacienteRepository;

    @Override
    public Optional<Paciente> findByDni(String dni) {
        return pacienteRepository.findByDni(dni)
                .map(PacienteMapper::toModel);
    }

    @Override
    public List<Paciente> findByNombreContainingIgnoreCase(String nombre) {
        List<PacienteEntity> pacienteEntityList = pacienteRepository.findByNombreContainingIgnoreCase(nombre);
        return pacienteEntityList.stream()
                .map(PacienteMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Paciente> buscarPorDniConSP(String dni) {
        return pacienteRepository.buscarPorDniConSP(dni)
                .map(PacienteMapper::toModel);
    }

    @Override
    public List<Paciente> buscarPorNombreConSP(String nombre) {
        return pacienteRepository.buscarPorNombreConSP(nombre).stream()
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
        PacienteEntity entity = PacienteMapper.toEntity(paciente);
        PacienteEntity updated = pacienteRepository.save(entity);
        return PacienteMapper.toModel(updated);
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
}
