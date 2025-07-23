package microservice.pacientes.application.domain.port.in;

import microservice.pacientes.application.domain.model.Paciente;

public interface PacientePortInWrite {

    Paciente crearPaciente(Paciente paciente);

    void eliminarPaciente(Long id);

    Paciente actualizarPaciente(Long id, Paciente paciente);

    Paciente desactivarPaciente(Long id);

    Paciente activarPaciente(Long id);
}
