package microservice.pacientes.application.service;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.command.mapper.CreatePacienteMapper;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacientePortOut;
import microservice.pacientes.application.domain.port.out.PacienteUpdater;
import microservice.pacientes.shared.annotations.UseCase;
import microservice.pacientes.shared.exception.PacienteDuplicadoException;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;
import java.util.List;

@AllArgsConstructor
@UseCase
public class PacienteService implements FindPacienteUseCase, CreatePacienteUseCase, UpdatePacienteUseCase, DeletePacienteUseCase {
    private final PacientePortOut pacientePortOut;
    private final PacienteUpdater pacienteUpdater;
    private final CreatePacienteMapper createPacienteMapper;

    @Override
    public Paciente create(CreatePacienteCommand createPacienteCommand) throws PacienteDuplicadoException {
        Paciente paciente = createPacienteMapper.toEntity(createPacienteCommand);
        if (pacientePortOut.existsByDni(paciente.getDni()))
            throw new PacienteDuplicadoException();
        return pacientePortOut.save(paciente);
    }

    @Override
    public void delete(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacientePortOut.getByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);
        pacientePortOut.delete(paciente);
    }

    @Override
    public List<Paciente> getAll() {
        return pacientePortOut.getAll();
    }

    @Override
    public List<Paciente> getByNombreContainingIgnoreCase(String nombre) {
        List<Paciente> pacientes = pacientePortOut.getByNombreContainingIgnoreCase(nombre);
        return pacientes;
    }

    @Override
    public Paciente getByDni(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacientePortOut.getByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);
        return paciente;
    }

    @Override
    public Paciente getByNombre(String nombre) throws PacienteNoEncontradoException {
        Paciente paciente = pacientePortOut.getByNombre(nombre)
                .orElseThrow(PacienteNoEncontradoException::new);
        return paciente;
    }

    @Override
    public List<Paciente> getByObraSocial(String obraSocial, int limit, int offset) throws PacienteNoEncontradoException {
        List<Paciente> pacientes = pacientePortOut.getByObraSocial(obraSocial, limit, offset);
        return pacientes;
    }

    @Override
    public Paciente update(String dni, UpdatePacienteCommand updatePacienteCommand) throws PacienteNoEncontradoException {
        Paciente paciente = pacientePortOut.getByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);
        pacienteUpdater.update(updatePacienteCommand, paciente);

        return pacientePortOut.save(paciente);
    }
}
