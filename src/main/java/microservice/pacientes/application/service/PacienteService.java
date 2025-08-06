package microservice.pacientes.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservice.pacientes.application.domain.command.CreatePacienteCommand;
import microservice.pacientes.application.domain.command.UpdatePacienteCommand;
import microservice.pacientes.application.domain.command.mapper.CreatePacienteMapper;
import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.port.in.CreatePacienteUseCase;
import microservice.pacientes.application.domain.port.in.DeletePacienteUseCase;
import microservice.pacientes.application.domain.port.in.FindPacienteUseCase;
import microservice.pacientes.application.domain.port.in.UpdatePacienteUseCase;
import microservice.pacientes.application.domain.port.out.LoggerPort;
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
    private final LoggerPort loggerPort;
    private static final String CLASS_NAME = "PacienteService";

    @Override
    public Paciente create(CreatePacienteCommand createPacienteCommand) throws PacienteDuplicadoException {
        loggerPort.setOrigin(CLASS_NAME + " - create");
        Paciente paciente = createPacienteMapper.toEntity(createPacienteCommand);
        if (pacientePortOut.existsByDni(paciente.getDni())) {
            loggerPort.error("Paciente didn't create because DNI is already in use. "+paciente.toString());
            throw new PacienteDuplicadoException();
        }
        loggerPort.info("Paciente created successfully. "+paciente.toString());
        return pacientePortOut.save(paciente);
    }

    @Override
    public void delete(String dni) throws PacienteNoEncontradoException {
        loggerPort.setOrigin(CLASS_NAME + " - delete");
        loggerPort.info("Starting deletion of patient with DNI: " + dni);

        Paciente paciente = pacientePortOut.getByDni(dni)
                .orElseThrow(() -> {
                    loggerPort.warn("Attempt to delete non-existent patient with DNI: "+ dni);
                    return new PacienteNoEncontradoException();
                });

        pacientePortOut.delete(paciente);
        loggerPort.info("Patient with DNI "+dni+" deleted successfully.");
    }

    @Override
    public List<Paciente> getAll() {
        loggerPort.setOrigin(CLASS_NAME + " - getAll");
        loggerPort.info("Fetching all patients.");
        return pacientePortOut.getAll();
    }

    @Override
    public List<Paciente> getByNombreContainingIgnoreCase(String nombre) {
        loggerPort.setOrigin(CLASS_NAME + " - getByNombreContainingIgnoreCase");
        loggerPort.info("Searching for patients with name containing: "+ nombre);
        List<Paciente> pacientes = pacientePortOut.getByNombreContainingIgnoreCase(nombre);
        if (pacientes.isEmpty()) {
            loggerPort.info("Search for patients with term "+nombre+" yielded no results.");
        }
        return pacientes;
    }

    @Override
    public Paciente getByDni(String dni) throws PacienteNoEncontradoException {
        loggerPort.setOrigin(CLASS_NAME + " - getByDni");
        loggerPort.info("Searching for patient by DNI: "+ dni);
        return pacientePortOut.getByDni(dni)
                .orElseThrow(() -> {
                    loggerPort.warn("Patient with DNI not found: "+ dni);
                    return new PacienteNoEncontradoException();
                });
    }

    @Override
    public Paciente getByNombre(String nombre) throws PacienteNoEncontradoException {
        loggerPort.setOrigin(CLASS_NAME + " - getByNombre");
        loggerPort.info("Searching for patient by name: "+ nombre);
        return pacientePortOut.getByNombre(nombre)
                .orElseThrow(() -> {
                    loggerPort.warn("Patient with name not found: "+ nombre);
                    return new PacienteNoEncontradoException();
                });
    }

    @Override
    public List<Paciente> getByObraSocial(String obraSocial, int limit, int offset) throws PacienteNoEncontradoException {
        loggerPort.setOrigin(CLASS_NAME + " - getByObraSocial");
        loggerPort.info("Searching for patients by health insurance: "+obraSocial+", with limit: "+limit+" and offset: "+offset);
        List<Paciente> pacientes = pacientePortOut.getByObraSocial(obraSocial, limit, offset);
        if (pacientes.isEmpty()) {
            loggerPort.info("Search for patients by health insurance "+obraSocial+" yielded no results.");
        }
        return pacientes;
    }

    @Override
    public Paciente update(String dni, UpdatePacienteCommand updatePacienteCommand) throws PacienteNoEncontradoException {
        loggerPort.setOrigin(CLASS_NAME + " - update");
        loggerPort.info("Starting update of patient with DNI: "+ dni);

        Paciente paciente = pacientePortOut.getByDni(dni)
                .orElseThrow(() -> {
                    loggerPort.warn("Attempt to update non-existent patient with DNI: "+ dni);
                    return new PacienteNoEncontradoException();
                });

        pacienteUpdater.update(updatePacienteCommand, paciente);
        Paciente pacienteActualizado = pacientePortOut.save(paciente);

        loggerPort.info("Patient with DNI "+dni+" updated successfully.");
        return pacienteActualizado;
    }
}
