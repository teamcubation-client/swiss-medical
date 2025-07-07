package com.teamcubation.api.pacientes.service;

import com.teamcubation.api.pacientes.exception.PacienteNoEncontradoException;
import com.teamcubation.api.pacientes.model.Paciente;
import com.teamcubation.api.pacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService implements IPacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public Paciente crear(Paciente paciente) {
        return pacienteRepository.guardar(paciente);
    }

    @Override
    public List<Paciente> obtenerTodos(String dni, String nombre) {
        return this.pacienteRepository.buscarTodos(dni, nombre);
    }

    @Override
    public Paciente obtenerPorID(long id) {
        Paciente paciente = this.pacienteRepository.buscarPorID(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
        return paciente;
    }

    @Override
    public Paciente actualizarPorID(long id, Paciente paciente) {
        Paciente existente = pacienteRepository.buscarPorID(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));

        copiarCamposNoNulos(paciente, existente);
        boolean actualizado = pacienteRepository.actualizarPorID(id, paciente);

        if (!actualizado) {
            throw new PacienteNoEncontradoException(id);
        }

        return paciente;
    }

    @Override
    public void borrarPorID(long id) {
        pacienteRepository.buscarPorID(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
        pacienteRepository.borrarPorID(id);
    }

    /**
     * Copia solo los campos no nulos desde el objeto de datos actualizados hacia el paciente existente.
     *
     * Este método permite realizar una actualización parcial del paciente,
     * manteniendo los valores originales de aquellos campos que no fueron modificados.
     *
     * Es útil en operaciones donde no se requiere sobrescribir todos los datos.
     *
     * @param datosActualizados Objeto que contiene los nuevos valores (puede tener campos nulos).
     * @param pacienteExistente Entidad persistida a la que se le aplicarán los cambios no nulos.
     */
    private void copiarCamposNoNulos(Paciente datosActualizados, Paciente pacienteExistente) {
        if (datosActualizados.getNombre() != null) pacienteExistente.setNombre(datosActualizados.getNombre());
        if (datosActualizados.getApellido() != null) pacienteExistente.setApellido(datosActualizados.getApellido());
        if (datosActualizados.getDni() != null) pacienteExistente.setDni(datosActualizados.getDni());
        if (datosActualizados.getObraSocial() != null) pacienteExistente.setObraSocial(datosActualizados.getObraSocial());
        if (datosActualizados.getEmail() != null) pacienteExistente.setEmail(datosActualizados.getEmail());
        if (datosActualizados.getTelefono() != null) pacienteExistente.setTelefono(datosActualizados.getTelefono());
    }

}
