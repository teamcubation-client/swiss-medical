package com.practica.crud_pacientes.controlador;

import com.practica.crud_pacientes.dto.PacienteDto;
import com.practica.crud_pacientes.dto.PacienteMapper;
import com.practica.crud_pacientes.modelo.Paciente;
import com.practica.crud_pacientes.servicio.IPacienteServicio;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Paciente")
public class PacienteControlador implements PacienteAPI{

    private final IPacienteServicio pacienteServicio;

    public PacienteControlador(IPacienteServicio pacienteServicio) {
        this.pacienteServicio = pacienteServicio;
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDto>> getPacientes() {
        List<Paciente> pacienteObtenido = pacienteServicio.getPacientes();
        List<PacienteDto> mapped = pacienteObtenido.stream()
                .map(PacienteMapper.mapper::pacienteToPacienteDto)
                .toList();
        return ResponseEntity.ok(mapped);
    }

    @GetMapping("/buscar-por-id/{id}")
    public ResponseEntity<PacienteDto> getPaciente(@PathVariable int id) {
        Paciente pacienteEncontrado = pacienteServicio.getPacientePorId(id);
        return ResponseEntity.ok(PacienteMapper.mapper.pacienteToPacienteDto(pacienteEncontrado));
    }

    @GetMapping("/buscar-por-dni")
    public ResponseEntity<PacienteDto> getPacientePorDni(@RequestParam @Valid String dni) {
        Paciente pacienteEncontrado = pacienteServicio.getPacientePorDni(dni);
        return ResponseEntity.ok(PacienteMapper.mapper.pacienteToPacienteDto(pacienteEncontrado));
    }

    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<PacienteDto>> getPacientePorNombre(@RequestParam @Valid String nombre) {
        List<Paciente> pacientesEncontrados = pacienteServicio.getPacientePorNombre(nombre);
        return ResponseEntity.ok(pacientesEncontrados.stream()
                .map(PacienteMapper.mapper::pacienteToPacienteDto)
                .toList());
    }

    @PostMapping("/nuevo-paciente")
    public ResponseEntity<PacienteDto> agregarPaciente(@RequestBody @Valid PacienteDto pacienteNuevo) {
        Paciente paciente = PacienteMapper.mapper.pacienteDtoToPaciente(pacienteNuevo);
        Paciente pacienteGuardado = pacienteServicio.addPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PacienteMapper.mapper.pacienteToPacienteDto(pacienteGuardado));
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<PacienteDto> actualizarPaciente(@PathVariable int id, @RequestBody PacienteDto pacienteDto) {
        Paciente paciente = PacienteMapper.mapper.pacienteDtoToPaciente(pacienteDto);
        Paciente pacienteActualizado = pacienteServicio.updatePaciente(id, paciente);
        return ResponseEntity.ok(PacienteMapper.mapper.pacienteToPacienteDto(pacienteActualizado));
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable int id) {
        pacienteServicio.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }
}
