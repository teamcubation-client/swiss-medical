package swissmedical.controller;

import swissmedical.dto.PacienteDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import swissmedical.model.Paciente;
import swissmedical.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import swissmedical.mapper.PacienteMapper;

@RestController
@RequestMapping("/api/pacientes")

public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteMapper pacienteMapper;

    @GetMapping
    public List<PacienteDTO> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarPacientes();
        List<PacienteDTO> dtos = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            dtos.add(pacienteMapper.toDTO(paciente));
        }
        return dtos;
    }
    
    @PostMapping
    public PacienteDTO crearPaciente(@RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        Paciente creado = pacienteService.crearPaciente(paciente);
        return pacienteMapper.toDTO(creado);
    }

    @GetMapping("/{id}")
    public PacienteDTO obtenerPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id);
        if (paciente != null) {
            return pacienteMapper.toDTO(paciente);
        } else {
            return null;
        }
    }
    
    @DeleteMapping("/{id}")
    public void eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
    }

    @GetMapping("/buscar/dni")
    public PacienteDTO buscarPorDni(@RequestParam String dni) {
        Paciente paciente = pacienteService.buscarPorDni(dni);
        if (paciente != null) {
            return pacienteMapper.toDTO(paciente);
        } else {
            return null;
        }
    }

    @GetMapping("/buscar/nombre")
    public List<PacienteDTO> buscarPorNombre(@RequestParam String nombre) {
        List<Paciente> pacientes = pacienteService.buscarPorNombreParcial(nombre);
        List<PacienteDTO> pacienteDTOs = new ArrayList<>();
        for (Paciente paciente : pacientes) {
            pacienteDTOs.add(pacienteMapper.toDTO(paciente));
        }
        return pacienteDTOs;
    }

    @PutMapping("/{id}")
    public PacienteDTO actualizarPaciente(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO) {
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);
        Paciente actualizado = pacienteService.actualizarPaciente(id, paciente);

        if (actualizado != null) {
            return pacienteMapper.toDTO(actualizado);
        } else {
            return null;
        }
    }
} 