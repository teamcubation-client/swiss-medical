package com.practica.crud_pacientes.controller;

import com.practica.crud_pacientes.dto.PacienteDto;
import com.practica.crud_pacientes.dto.PacienteMapper;
import com.practica.crud_pacientes.model.Paciente;
import com.practica.crud_pacientes.service.IPacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PacienteController implements PacienteAPI{

    private final IPacienteService pacienteService;

    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDto>> getPacientes() {
        List<Paciente> obtainedPacientes = pacienteService.getPacientes();
        List<PacienteDto> mapped = obtainedPacientes.stream()
                .map(PacienteMapper.mapper::pacienteToPacienteDto)
                .toList();
        return ResponseEntity.ok(mapped);
    }

    @GetMapping("/buscar-por-id/{id}")
    public ResponseEntity<PacienteDto> getPaciente(@PathVariable int id) {
        Paciente obtainedPaciente = pacienteService.getPacienteById(id);
        return ResponseEntity.ok(PacienteMapper.mapper.pacienteToPacienteDto(obtainedPaciente));
    }

    @GetMapping("/buscar-por-dni")
    public ResponseEntity<PacienteDto> getPacienteByDni(@RequestParam @Valid String dni) {
        Paciente obtainedPaciente = pacienteService.getPacienteByDni(dni);
        return ResponseEntity.ok(PacienteMapper.mapper.pacienteToPacienteDto(obtainedPaciente));
    }

    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<PacienteDto>> getPacienteByName(@RequestParam @Valid String nombre) {
        List<Paciente> obtainedPacientes = pacienteService.getPacienteByName(nombre);
        return ResponseEntity.ok(obtainedPacientes.stream()
                .map(PacienteMapper.mapper::pacienteToPacienteDto)
                .toList());
    }

    @PostMapping("/nuevo-paciente")
    public ResponseEntity<PacienteDto> addPaciente(@RequestBody @Valid PacienteDto pacienteNuevo) {
        Paciente paciente = PacienteMapper.mapper.pacienteDtoToPaciente(pacienteNuevo);
        Paciente savedPaciente = pacienteService.addPaciente(paciente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PacienteMapper.mapper.pacienteToPacienteDto(savedPaciente));
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<PacienteDto> updatePaciente(@PathVariable int id, @RequestBody PacienteDto pacienteDto) {
        Paciente paciente = PacienteMapper.mapper.pacienteDtoToPaciente(pacienteDto);
        Paciente updatedPaciente = pacienteService.updatePaciente(id, paciente);
        return ResponseEntity.ok(PacienteMapper.mapper.pacienteToPacienteDto(updatedPaciente));
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable int id) {
        pacienteService.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("pacientes/dni/{dni}")
    public ResponseEntity<PacienteDto> getPacientePorDniFromSP(@PathVariable @Valid String dni) {
        Paciente obtainedPaciente = pacienteService.getByDniFromSP(dni);
        return ResponseEntity.ok(PacienteMapper.mapper.pacienteToPacienteDto(obtainedPaciente));
    }

    @GetMapping("pacientes/nombre/{nombre}")
    public ResponseEntity<List<PacienteDto>> getPacientesByNombreFromSP(@PathVariable @Valid String nombre) {
        List<Paciente> obtainedPacientes = pacienteService.getPacientesbyNombreFromSP(nombre);
        return ResponseEntity.ok(obtainedPacientes.stream()
                .map(PacienteMapper.mapper::pacienteToPacienteDto)
                .toList());
    }

    @GetMapping("pacientes/obra-social/{obraSocial}/{limite}/{off}")
    public ResponseEntity<List<PacienteDto>> getPacientesByObraSocialFromSP(@PathVariable @Valid String obraSocial, @PathVariable int limite, @PathVariable int off) {
        List<Paciente> obtainedPacientes = pacienteService.getPacietesbyObraSocialFromSP(obraSocial, limite, off);
        return ResponseEntity.ok(obtainedPacientes.stream()
                .map(PacienteMapper.mapper::pacienteToPacienteDto)
                .toList());
    }

}
