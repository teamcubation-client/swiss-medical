package com.teamcubation.api.pacientes.service;

import com.teamcubation.api.pacientes.dto.PacienteRequest;
import com.teamcubation.api.pacientes.dto.PacienteResponse;
import com.teamcubation.api.pacientes.exception.PacienteNoEncontradoException;
import com.teamcubation.api.pacientes.model.Paciente;
import com.teamcubation.api.pacientes.repository.IPacienteRepository;
import com.teamcubation.api.pacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public PacienteResponse crearPaciente(PacienteRequest request) {
        Paciente paciente = mapToEntity(request);
        Paciente pacienteGuardado = pacienteRepository.guardar(paciente);
        PacienteResponse response = mapToResponse(pacienteGuardado);
        return response;
    }

    @Override
    public List<PacienteResponse> obtenerPacientes() {
        List<Paciente> pacientes = this.pacienteRepository.buscarTodos();
        List<PacienteResponse> response = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            PacienteResponse pacienteResponse = mapToResponse(paciente);
            response.add(pacienteResponse);
        }
        return response;
    }

    @Override
    public PacienteResponse obtenerPacientePorId(Long id) {
        Paciente paciente = this.pacienteRepository.buscarPorId(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));
        return mapToResponse(paciente);
    }

    @Override
    public PacienteResponse actualizarPaciente(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.buscarPorId(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(id));

        copiarCamposNoNulos(request, paciente);

        boolean actualizado = pacienteRepository.actualizar(paciente);

        if (!actualizado) {
            throw new PacienteNoEncontradoException(id);
        }

        return mapToResponse(paciente);
    }

    @Override
    public void borrarPaciente(Long id) {
        boolean borrado = pacienteRepository.borrar(id);

        if (!borrado) {
            throw new PacienteNoEncontradoException(id);
        }
    }

    private Paciente mapToEntity(PacienteRequest request) {
        Paciente paciente = new Paciente();
        paciente.setNombre(request.getNombre());
        paciente.setApellido(request.getApellido());
        paciente.setDni(request.getDni());
        paciente.setObraSocial(request.getObraSocial());
        paciente.setEmail(request.getEmail());
        paciente.setTelefono(request.getTelefono());
        return paciente;
    }

    private PacienteResponse mapToResponse(Paciente p) {
        PacienteResponse pacienteResponse = new PacienteResponse();
        pacienteResponse.setId(p.getId());
        pacienteResponse.setNombre(p.getNombre());
        pacienteResponse.setApellido(p.getApellido());
        pacienteResponse.setDni(p.getDni());
        return pacienteResponse;
    }

    private void copiarCamposNoNulos(PacienteRequest request, Paciente paciente) {
        if (request.getNombre() != null) paciente.setNombre(request.getNombre());
        if (request.getApellido() != null) paciente.setApellido(request.getApellido());
        if (request.getDni() != null) paciente.setDni(request.getDni());
        if (request.getObraSocial() != null) paciente.setObraSocial(request.getObraSocial());
        if (request.getEmail() != null) paciente.setEmail(request.getEmail());
        if (request.getTelefono() != null) paciente.setTelefono(request.getTelefono());
    }
}
