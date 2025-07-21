package microservice.pacientes.application.domain.factory;

import lombok.AllArgsConstructor;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import microservice.pacientes.shared.annotations.Factory;

@AllArgsConstructor
@Factory
public class PacienteFactoryImpl implements PacienteFactory {
    private final PacienteValidator pacienteValidator;
    @Override
    public Paciente create(String dni, String nombre, String apellido, String obraSocial, String email, String telefono) {
        Paciente paciente = new Paciente(dni, nombre, apellido, obraSocial, email, telefono);
        pacienteValidator.validate(paciente);
        return paciente;
    }
}
