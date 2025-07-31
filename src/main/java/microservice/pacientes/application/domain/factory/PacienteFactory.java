package microservice.pacientes.application.domain.factory;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;

public interface PacienteFactory {
    Paciente create(String dni, String nombre, String apellido, String obraSocial, String email, String telefono) throws PacienteArgumentoInvalido;
}
