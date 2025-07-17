package com.teamcubation.api.pacientes.service;

import com.teamcubation.api.pacientes.exception.PacienteDuplicadoException;
import com.teamcubation.api.pacientes.exception.PacienteNoEncontradoException;
import com.teamcubation.api.pacientes.model.Paciente;
import com.teamcubation.api.pacientes.repository.IPacienteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {

    private final IPacienteRepository pacienteRepository;
    private static final String CAMPO_DNI = "DNI";

    public PacienteService(IPacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    @Transactional
    public Paciente crear(Paciente paciente) {
        Optional<Paciente> existente = pacienteRepository.findByDniSP(paciente.getDni());

        if (existente.isPresent()) {
            throw new PacienteDuplicadoException(CAMPO_DNI, paciente.getDni());
        }
        return pacienteRepository.save(paciente);
    }

    @Override
    public List<Paciente> obtenerTodos(String dni, String nombre) {
        return this.pacienteRepository.buscarTodos(dni, nombre);
    }

    @Override
    public Paciente obtenerPorID(long id) {
        return this.pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
    }

    @Override
    public Paciente obtenerPorDNI(String dni) {
        return pacienteRepository.findByDniSP(dni)
                .orElseThrow(() -> new PacienteNoEncontradoException("DNI: " + dni));
    }

    @Override
    public List<Paciente> obtenerPorNombre(String nombre) {
        return pacienteRepository.findByNombreSP(nombre);
    }

    @Override
    public List<Paciente> obtenerPorObraSocial(String obraSocial, int page, int size) {
        return pacienteRepository.findByObraSocialPaginadoSP(obraSocial, size, page * size);
    }

    @Override
    @Transactional
    public Paciente actualizarPorID(long id, Paciente datosActualizados) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));

        if (datosActualizados.getDni() != null) {
            Optional<Paciente> pacienteDNIDuplicado = pacienteRepository.findByDni(datosActualizados.getDni());
            boolean dniUsadoPorOtro = pacienteDNIDuplicado.isPresent() && !pacienteDNIDuplicado.get().getId().equals(id);
            if (dniUsadoPorOtro) {
                throw new PacienteDuplicadoException(CAMPO_DNI, datosActualizados.getDni());
            }
        }

        copiarCamposNoNulos(datosActualizados, paciente);
        return pacienteRepository.save(paciente);
    }

    @Override
    @Transactional
    public void borrarPorID(long id) {
        pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
        pacienteRepository.deleteById(id);
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
