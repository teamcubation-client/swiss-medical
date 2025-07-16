package microservice.pacientes.application.service;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.out.PacienteRepositoryPort;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;

import java.util.List;

@AllArgsConstructor
public class FindPacienteService implements FindPacienteUseCase {

    private final PacienteRepositoryPort pacienteRepositoryPort;

    @Override
    public List<Paciente> getAll() {
        return pacienteRepositoryPort.getAll();
    }

    @Override
    public List<Paciente> getByNombreContainingIgnoreCase(String nombre) {
        return pacienteRepositoryPort.getByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Paciente getByDni(String dni) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepositoryPort.getByDni(dni)
                .orElseThrow(PacienteNoEncontradoException::new);
        return paciente;
    }

    @Override
    public Paciente getByNombre(String nombre) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepositoryPort.getByNombre(nombre)
                .orElseThrow(PacienteNoEncontradoException::new);
        return paciente;
    }

    @Override
    public List<Paciente> getByObraSocial(String obraSocial, int limit, int offset) throws PacienteNoEncontradoException {
        List<Paciente> pacientes = pacienteRepositoryPort.getByObraSocial(obraSocial, limit, offset);
        return pacientes;
    }
}
