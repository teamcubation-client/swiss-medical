package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.controller;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.domain.port.in.IPatientPortIn;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/pacientes")
public class PatientController implements IPatientAPI {

    private final IPatientPortIn iPatientPortIn;

    PatientController(IPatientPortIn iPatientPortIn) {
        this.iPatientPortIn = iPatientPortIn;
    }

    @Override
    @PostMapping
    public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest request) {
        Patient patient = PatientRestMapper.toDomain(request);
        Patient created = this.iPatientPortIn.create(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(PatientRestMapper.toResponse(created));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll(@RequestParam(required = false) String dni,
                                                        @RequestParam(value = "nombre", required = false) String name) {
        List<Patient> patients = this.iPatientPortIn.getAll(dni, name);
        List<PatientResponse> response = new ArrayList<>();
        for (Patient patient : patients) {
            response.add(PatientRestMapper.toResponse(patient));
        }
        return ResponseEntity.ok().body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable long id) {
        Patient patient = this.iPatientPortIn.getById(id);
        return ResponseEntity.ok().body(PatientRestMapper.toResponse(patient));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updateById(@PathVariable long id,
                                                      @RequestBody PatientRequest request) {
        Patient patient = PatientRestMapper.toDomain(request);
        Patient response = this.iPatientPortIn.updateById(id, patient);
        return ResponseEntity.ok().body(PatientRestMapper.toResponse(response));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        this.iPatientPortIn.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/dni/{dni}")
    public ResponseEntity<PatientResponse> getByDni(@PathVariable String dni) {
        Patient response = this.iPatientPortIn.getByDni(dni);
        return ResponseEntity.ok(PatientRestMapper.toResponse(response));
    }

    @Override
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<PatientResponse>> getByName(@PathVariable("nombre") String name) {
        List<Patient> patients = this.iPatientPortIn.getByName(name);
        List<PatientResponse> response = new ArrayList<>();
        for(Patient patient : patients) {
            response.add(PatientRestMapper.toResponse(patient));
        }
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/obra-social")
    public ResponseEntity<List<PatientResponse>> getByHealthInsuranceProvider(@RequestParam("obra_social") String healthInsuranceProvider,
                                                                              @RequestParam int page,
                                                                              @RequestParam int size) {
        List<Patient> patients = this.iPatientPortIn.getByHealthInsuranceProvider(healthInsuranceProvider,page,size);
        List<PatientResponse> response = new ArrayList<>();
        for(Patient patient : patients){
            response.add(PatientRestMapper.toResponse(patient));
        }
        return ResponseEntity.ok(response);
    }
}
