package microservice.pacientes.application.domain.port.out;

import microservice.pacientes.application.domain.model.Paciente;

public interface PacientePortOutWrite {
    Paciente save(Paciente paciente);

    Paciente update(Paciente paciente);

    void deleteById(long id);
}
