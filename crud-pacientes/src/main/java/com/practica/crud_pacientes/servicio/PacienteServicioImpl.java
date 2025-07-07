package com.practica.crud_pacientes.servicio;

import com.practica.crud_pacientes.dto.PacienteDto;
import com.practica.crud_pacientes.dto.PacienteMapper;
import com.practica.crud_pacientes.excepciones.PacienteDuplicadoException;
import com.practica.crud_pacientes.excepciones.PacienteNoEncontradoException;
import com.practica.crud_pacientes.modelo.Paciente;
import com.practica.crud_pacientes.repositorio.PacienteRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteServicioImpl implements IPacienteServicio {

    private final PacienteRepositorio pacienteRepositorio;

    public PacienteServicioImpl(PacienteRepositorio pacienteRepositorio) {
        this.pacienteRepositorio = pacienteRepositorio;
    }

    @Override
    public List<PacienteDto> getPacientes() {
        List<Paciente> pacientes = pacienteRepositorio.findAll();

        return pacientes.stream().map(
                PacienteMapper.mapper::pacienteToPacienteDto).collect(Collectors.toList());
    }

    @Override
    public PacienteDto getPacientePorId(Integer idPaciente) throws PacienteNoEncontradoException {
        Paciente paciente = pacienteRepositorio.findById(idPaciente).orElseThrow(PacienteNoEncontradoException::new);

        return PacienteMapper.mapper.pacienteToPacienteDto(paciente);
    }

    @Override
    public PacienteDto getPacientePorDni(String dni) {
        Paciente paciente = pacienteRepositorio.findByDni(dni);

        if (paciente == null) {
            throw new PacienteNoEncontradoException();
        }
        return PacienteMapper.mapper.pacienteToPacienteDto(paciente);
    }

    @Override
    public List<PacienteDto> getPacientePorNombre(String nombre) {
        List<Paciente> pacientes = pacienteRepositorio.findByNombreContainingIgnoreCase(nombre);
        return pacientes.stream()
                .map(PacienteMapper.mapper::pacienteToPacienteDto)
                .collect(Collectors.toList());
    }

    @Override
    public PacienteDto addPaciente(PacienteDto pacienteDto) {
        if(pacienteRepositorio.findByDni(pacienteDto.getDni()) != null){
            throw new PacienteDuplicadoException();
        }

        Paciente paciente = PacienteMapper.mapper.pacienteDtoToPaciente(pacienteDto);
        Paciente pacienteGuardado = pacienteRepositorio.save(paciente);
        return PacienteMapper.mapper.pacienteToPacienteDto(pacienteGuardado);
    }

    @Override
    public PacienteDto updatePaciente(Integer idPaciente, PacienteDto pacienteDto) throws PacienteNoEncontradoException {

        pacienteRepositorio.findById(idPaciente)
                .orElseThrow(PacienteNoEncontradoException::new);

        Paciente pacienteExistente = pacienteRepositorio.findByDni(pacienteDto.getDni());
        if(pacienteExistente != null && !pacienteExistente.getIdPaciente().equals(idPaciente))
            throw new PacienteDuplicadoException();

        Paciente pacienteActualizado = PacienteMapper.mapper.pacienteDtoToPaciente(pacienteDto);
        pacienteActualizado.setIdPaciente(idPaciente);

        Paciente pacienteGuardado = pacienteRepositorio.save(pacienteActualizado);
        return PacienteMapper.mapper.pacienteToPacienteDto(pacienteGuardado);
    }

    @Override
    public void deletePaciente(Integer idPaciente) throws PacienteNoEncontradoException {
        if(!pacienteRepositorio.existsById(idPaciente))
            throw new PacienteNoEncontradoException();

        pacienteRepositorio.deleteById(idPaciente);
    }
}
