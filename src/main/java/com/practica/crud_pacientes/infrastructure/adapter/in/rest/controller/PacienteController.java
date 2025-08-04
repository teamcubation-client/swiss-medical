package com.practica.crud_pacientes.infrastructure.adapter.in.rest.controller;

import com.practica.crud_pacientes.application.domain.port.in.TrafficNotifier;
import com.practica.crud_pacientes.application.domain.port.out.PacienteLoggerPort;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/pacientes")
public class PacienteController implements PacienteAPI {

    private final PacienteUseCase pacienteUseCase;
    private final TrafficNotifier trafficNotifier;
    private final PacienteRestMapper mapper;
    private final PacienteLoggerPort loggerPort;


    @GetMapping
    public ResponseEntity<List<PacienteResponse>> getPacientes(){
        loggerPort.info("GET /pacientes - Listando todos los pacientes");
        trafficNotifier.notify("/pacientes");
        List<Paciente> pacientesDomain = pacienteUseCase.getPacientes();
        loggerPort.info("Se encontraron {} pacientes", pacientesDomain.size());
        return ResponseEntity.ok(mapToDomainList(pacientesDomain));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> getPaciente(@PathVariable int id) {
        loggerPort.info("GET /pacientes/{} - Buscando paciente por ID", id);
        Paciente paciente = pacienteUseCase.getPacienteById(id);
        loggerPort.info("Paciente encontrado: {}", paciente);
        return ResponseEntity.ok(mapper.domainToResponse(paciente));
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> addPaciente(@RequestBody @Valid PacienteRequest pacienteNuevo) {
        loggerPort.info("POST /pacientes - Creando nuevo paciente: {}", pacienteNuevo);
        Paciente pacienteDomain = mapper.requestToDomain(pacienteNuevo);
        Paciente nuevoPaciente = pacienteUseCase.addPaciente(pacienteDomain);
        loggerPort.info("Paciente creado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.domainToResponse(nuevoPaciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> updatePaciente(@PathVariable int id, @RequestBody PacienteRequest paciente) {
        loggerPort.info("PUT /pacientes/{} - Actualizando paciente con datos: {}", id, paciente);
        Paciente pacienteDomain = mapper.requestToDomain(paciente);
        Paciente pacienteActualizado = pacienteUseCase.updatePaciente(id, pacienteDomain);
        loggerPort.info("Paciente con ID {} actualizado correctamente", id);
        return ResponseEntity.ok(mapper.domainToResponse(pacienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable int id) {
        loggerPort.info("DELETE /pacientes/{} - Eliminando paciente", id);
        pacienteUseCase.deletePaciente(id);
        loggerPort.info("Paciente con ID {} eliminado correctamente", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponse> getPacienteByDni(@PathVariable String dni) {
        loggerPort.info("GET /pacientes/dni/{} - Buscando paciente por DNI", dni);
        Paciente paciente = pacienteUseCase.getPacienteByDni(dni);
        loggerPort.info("Paciente encontrado con DNI {}: {}", dni, paciente);
        return ResponseEntity.ok(mapper.domainToResponse(paciente));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<PacienteResponse>> getPacientesByName(@PathVariable String nombre) {
        loggerPort.info("GET /pacientes/nombre/{} - Buscando pacientes por nombre", nombre);
        List<Paciente> pacientes = pacienteUseCase.getPacientesByName(nombre);
        loggerPort.info("Se encontraron {} pacientes con nombre {}", pacientes.size(), nombre);
        return ResponseEntity.ok(mapToDomainList(pacientes));
    }

    @GetMapping("/obra-social/{obraSocial}")
    public ResponseEntity<List<PacienteResponse>> getPacientesByObraSocial(@PathVariable @Valid String obraSocial,
                                                                      @RequestParam(defaultValue = "1") int limite,
                                                                      @RequestParam(defaultValue = "0") int off) {
        loggerPort.info("GET /pacientes/obra-social/{}?limite={}&off={} - Buscando pacientes por obra social", obraSocial, limite, off);
        List<Paciente> pacientes = pacienteUseCase.getPacientesByObraSocial(obraSocial, limite, off);
        loggerPort.info("Se encontraron {} pacientes con obra social {}", pacientes.size(), obraSocial);
        return ResponseEntity.ok(mapToDomainList(pacientes));
    }

    private List<PacienteResponse> mapToDomainList(List<Paciente> pacientes) {
        return pacientes.stream()
                .map(mapper::domainToResponse)
                .toList();
    }

}
