package com.teamcubation.api.pacientes.controller;

import com.teamcubation.api.pacientes.dto.PacienteRequest;
import com.teamcubation.api.pacientes.dto.PacienteResponse;
import com.teamcubation.api.pacientes.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> crearPaciente(@RequestBody PacienteRequest request) {
        PacienteResponse paciente = this.pacienteService.crearPaciente(request);
        return new ResponseEntity<>(paciente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> obtenerPacientes() {
        List<PacienteResponse> pacientes = this.pacienteService.obtenerPacientes();
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> obtenerPaciente(@PathVariable Long id) {
        PacienteResponse paciente = this.pacienteService.obtenerPacientePorId(id);
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> actualizarPaciente(@PathVariable Long id, @RequestBody PacienteRequest request) {
        PacienteResponse paciente = this.pacienteService.actualizarPaciente(id, request);
        return new ResponseEntity<>(paciente, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrarPaciente(@PathVariable Long id) {
        this.pacienteService.borrarPaciente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}