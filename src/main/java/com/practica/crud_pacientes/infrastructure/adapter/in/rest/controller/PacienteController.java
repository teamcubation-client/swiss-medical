package com.practica.crud_pacientes.infrastructure.adapter.in.rest.controller;

import com.practica.crud_pacientes.application.domain.port.in.Mediator;
import jakarta.validation.Valid;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.in.PacienteUseCase;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteResponse;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.mapper.PacienteRestMapper;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.openAPI.PacienteAPI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/pacientes")
public class PacienteController implements PacienteAPI {

    private final PacienteUseCase pacienteUseCase;
    private final Mediator mediator;


    @GetMapping("")
    public ResponseEntity<List<PacienteResponse>> getPacientes(){
        mediator.notifyTraffic("/pacientes");
        List<Paciente> pacientesDomain = pacienteUseCase.getPacientes();
        List<PacienteResponse> pacientesResponse = pacientesDomain.stream()
                .map(PacienteRestMapper.mapper::domainToResponse)
                .toList();
        return ResponseEntity.ok(pacientesResponse);
    }

    @GetMapping("/buscar-por-id/{id}")
    public ResponseEntity<PacienteResponse> getPaciente(@PathVariable int id) {
        Paciente paciente = pacienteUseCase.getPacienteById(id);
        return ResponseEntity.ok(PacienteRestMapper.mapper.domainToResponse(paciente));
    }

    @PostMapping("/nuevo-paciente")
    public ResponseEntity<PacienteResponse> addPaciente(@RequestBody @Valid PacienteRequest pacienteNuevo) {
        Paciente pacienteDomain = PacienteRestMapper.mapper.requestToDomain(pacienteNuevo);
        Paciente nuevoPaciente = pacienteUseCase.addPaciente(pacienteDomain);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PacienteRestMapper.mapper.domainToResponse(nuevoPaciente));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PacienteResponse> updatePaciente(@PathVariable int id, @RequestBody PacienteRequest paciente) {
        Paciente pacienteDomain = PacienteRestMapper.mapper.requestToDomain(paciente);
        Paciente pacienteActualizado = pacienteUseCase.updatePaciente(id, pacienteDomain);
        return ResponseEntity.ok(PacienteRestMapper.mapper.domainToResponse(pacienteActualizado));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable int id) {
        pacienteUseCase.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponse> getPacienteByDni(@PathVariable @Valid String dni) {
        Paciente paciente = pacienteUseCase.getByDni(dni);
        return ResponseEntity.ok(PacienteRestMapper.mapper.domainToResponse(paciente));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<PacienteResponse>> getPacientesByName(@PathVariable @Valid String nombre) {
        List<Paciente> pacientes = pacienteUseCase.getPacientesbyName(nombre);
        List<PacienteResponse> pacientesResponse = pacientes.stream()
                .map(PacienteRestMapper.mapper::domainToResponse)
                .toList();
        return ResponseEntity.ok(pacientesResponse);
    }

    @GetMapping("/obra-social/{obraSocial}")
    public ResponseEntity<List<PacienteResponse>> getPacientesByObraSocial(@PathVariable @Valid String obraSocial,
                                                                      @RequestParam(defaultValue = "1") int limite,
                                                                      @RequestParam(defaultValue = "0") int off) {
        List<Paciente> pacientes = pacienteUseCase.getPacietesbyObraSocial(obraSocial, limite, off);
        List<PacienteResponse> pacientesResponse = pacientes.stream()
                .map(PacienteRestMapper.mapper::domainToResponse)
                .toList();
        return ResponseEntity.ok(pacientesResponse);
    }

}
