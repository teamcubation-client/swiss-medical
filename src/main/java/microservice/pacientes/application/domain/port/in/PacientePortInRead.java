package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.application.domain.model.Paciente;

import java.util.List;

public interface PacientePortInRead {

    Paciente obtenerPacientePorId(Long id);

    List<Paciente> listarPacientesPorEstado(Boolean estado);

    List<Paciente> buscarPorNombreParcial(String nombre);

    Paciente buscarByDni(String dni);

    List<Paciente> buscarByNombre(String nombre);

    List<Paciente> buscarPorObraSocialPaginado(String obraSocial, int limit, int offset);
}
