package com.practica.crud_pacientes.infrastructure.adapter.in.rest.controller;

import com.practica.crud_pacientes.application.domain.port.in.TrafficNotifier;
import jakarta.validation.Valid;
import com.practica.crud_pacientes.application.domain.model.Paciente;
import com.practica.crud_pacientes.application.domain.port.in.PacienteUseCase;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteRequest;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.dto.PacienteResponse;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.mapper.PacienteRestMapper;
import com.practica.crud_pacientes.infrastructure.adapter.in.rest.openAPI.PacienteAPI;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/pacientes")
public class PacienteController implements PacienteAPI {

    private final PacienteUseCase pacienteUseCase;
    private final TrafficNotifier trafficNotifier;
    private final PacienteRestMapper mapper;


    @GetMapping
    public ResponseEntity<List<PacienteResponse>> getPacientes(){
        trafficNotifier.notify("/pacientes");
        List<Paciente> pacientesDomain = pacienteUseCase.getPacientes();
        return ResponseEntity.ok(mapToDomainList(pacientesDomain));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> getPaciente(@PathVariable int id) {
        Paciente paciente = pacienteUseCase.getPacienteById(id);
        return ResponseEntity.ok(mapper.domainToResponse(paciente));
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> addPaciente(@RequestBody @Valid PacienteRequest pacienteNuevo) {
        Paciente pacienteDomain = mapper.requestToDomain(pacienteNuevo);
        Paciente nuevoPaciente = pacienteUseCase.addPaciente(pacienteDomain);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.domainToResponse(nuevoPaciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> updatePaciente(@PathVariable int id, @RequestBody PacienteRequest paciente) {
        Paciente pacienteDomain = mapper.requestToDomain(paciente);
        Paciente pacienteActualizado = pacienteUseCase.updatePaciente(id, pacienteDomain);
        return ResponseEntity.ok(mapper.domainToResponse(pacienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable int id) {
        pacienteUseCase.deletePaciente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponse> getPacienteByDni(@PathVariable String dni) {
        Paciente paciente = pacienteUseCase.getPacienteByDni(dni);
        return ResponseEntity.ok(mapper.domainToResponse(paciente));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<PacienteResponse>> getPacientesByName(@PathVariable String nombre) {
        List<Paciente> pacientes = pacienteUseCase.getPacientesByName(nombre);
        return ResponseEntity.ok(mapToDomainList(pacientes));
    }

    @GetMapping("/obra-social/{obraSocial}")
    public ResponseEntity<List<PacienteResponse>> getPacientesByObraSocial(@PathVariable @Valid String obraSocial,
                                                                      @RequestParam(defaultValue = "1") int limite,
                                                                      @RequestParam(defaultValue = "0") int off) {
        List<Paciente> pacientes = pacienteUseCase.getPacientesByObraSocial(obraSocial, limite, off);
        return ResponseEntity.ok(mapToDomainList(pacientes));
    }

    private List<PacienteResponse> mapToDomainList(List<Paciente> pacientes) {
        return pacientes.stream()
                .map(mapper::domainToResponse)
                .toList();
    }

}
