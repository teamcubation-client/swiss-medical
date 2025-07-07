package com.practica.crud_pacientes.servicio;

import com.practica.crud_pacientes.excepciones.PacienteDuplicadoException;
import com.practica.crud_pacientes.excepciones.PacienteNoEncontradoException;
import com.practica.crud_pacientes.modelo.Paciente;
import com.practica.crud_pacientes.repositorio.PacienteRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServicioImpl implements IPacienteServicio {

    private final PacienteRepositorio pacienteRepositorio;

    public PacienteServicioImpl(PacienteRepositorio pacienteRepositorio) {
        this.pacienteRepositorio = pacienteRepositorio;
    }

    @Override
    public List<Paciente> getPacientes() {
        return pacienteRepositorio.findAll();
    }

    @Override
    public Paciente getPacientePorId(int id) throws PacienteNoEncontradoException {
        return pacienteRepositorio.findById(id).orElseThrow(PacienteNoEncontradoException::new);
    }

    @Override
    public Paciente getPacientePorDni(String dni) {
        Paciente paciente = pacienteRepositorio.findByDni(dni);

        if (paciente == null) {
            throw new PacienteNoEncontradoException();
        }
        return paciente;
    }

    @Override
    public List<Paciente> getPacientePorNombre(String nombre) {
        return pacienteRepositorio.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Paciente addPaciente(Paciente paciente) {
        if (pacienteRepositorio.findByDni(paciente.getDni()) != null) {
            throw new PacienteDuplicadoException();
        }
        return pacienteRepositorio.save(paciente);
    }

    @Override
    public Paciente updatePaciente(int id, Paciente paciente) throws PacienteNoEncontradoException {

        pacienteRepositorio.findById(id)
                .orElseThrow(PacienteNoEncontradoException::new);

        Paciente pacienteExistente = pacienteRepositorio.findByDni(paciente.getDni());
        if (pacienteExistente != null)
            throw new PacienteDuplicadoException();

        paciente.setId(id);
        return pacienteRepositorio.save(paciente);
    }

    @Override
    public void deletePaciente(int id) throws PacienteNoEncontradoException {
        if (!pacienteRepositorio.existsById(id))
            throw new PacienteNoEncontradoException();

        pacienteRepositorio.deleteById(id);
    }
}
