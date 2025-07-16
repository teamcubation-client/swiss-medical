package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteNoEncontradoException;

import java.util.List;

public interface FindPacienteUseCase {
    List<Paciente> getAll();
    List<Paciente> getByNombreContainingIgnoreCase(String nombre);
    Paciente getByDni(String dni) throws PacienteNoEncontradoException;
    Paciente getByNombre(String nombre) throws PacienteNoEncontradoException;
    List<Paciente> getByObraSocial(String obraSocial, int limit, int offset) throws PacienteNoEncontradoException;
}
