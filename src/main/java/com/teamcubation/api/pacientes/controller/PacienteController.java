package com.teamcubation.api.pacientes.controller;

import com.teamcubation.api.pacientes.dto.PacienteRequest;
import com.teamcubation.api.pacientes.dto.PacienteResponse;
import com.teamcubation.api.pacientes.mapper.PacienteMapper;
import com.teamcubation.api.pacientes.model.Paciente;
import com.teamcubation.api.pacientes.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/pacientes")
public class PacienteController implements IPacienteAPI {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService, PacienteMapper pacienteMapper) {
        this.pacienteService = pacienteService;
    }

    @Override
    @PostMapping
    public ResponseEntity<PacienteResponse> crear(@RequestBody PacienteRequest request) {
        Paciente paciente = PacienteMapper.toEntity(request);
        Paciente creado = this.pacienteService.crear(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(PacienteMapper.toResponse(creado));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PacienteResponse>> obtenerTodos(@RequestParam(required = false) String dni,
                                                               @RequestParam(required = false) String nombre) {
        List<Paciente> pacientes = this.pacienteService.obtenerTodos(dni, nombre);
        List<PacienteResponse> response = new ArrayList<>();
        for(Paciente paciente : pacientes) {
            response.add(PacienteMapper.toResponse(paciente));
        }
        return ResponseEntity.ok().body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> obtenerPorID(@PathVariable long id) {
        Paciente paciente = this.pacienteService.obtenerPorID(id);
        return ResponseEntity.ok().body(PacienteMapper.toResponse(paciente));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> actualizarPorID(@PathVariable long id,
                                                            @RequestBody PacienteRequest request) {
        Paciente paciente = PacienteMapper.toEntity(request);
        Paciente response = this.pacienteService.actualizarPorID(id, paciente);
        return ResponseEntity.ok().body(PacienteMapper.toResponse(response));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarPorID(@PathVariable long id) {
        this.pacienteService.borrarPorID(id);
        return ResponseEntity.noContent().build();
    }

}