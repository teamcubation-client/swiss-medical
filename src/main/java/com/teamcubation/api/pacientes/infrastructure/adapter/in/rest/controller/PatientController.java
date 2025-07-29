package com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.controller;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.domain.port.in.PatientPortIn;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.mapper.PatientRestMapper;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.ApiResponse;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.rest.response.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/pacientes")
public class PatientController implements IPatientAPI {

    private final PatientPortIn patientPortIn;

    PatientController(PatientPortIn patientPortIn) {
        this.patientPortIn = patientPortIn;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<PatientResponse>> create(@RequestBody PatientRequest request) {
        Patient created = this.patientPortIn.create(PatientRestMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(PatientRestMapper.toResponse(created)));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getAll(@RequestParam(required = false) String dni,
                                                                     @RequestParam(value = "nombre", required = false) String name) {
        List<Patient> patients = this.patientPortIn.getAll(dni, name);
        List<PatientResponse> response = new ArrayList<>();

        for (Patient patient : patients) {
            response.add(PatientRestMapper.toResponse(patient));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(response));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> getById(@PathVariable long id) {
        Patient patient = this.patientPortIn.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(PatientRestMapper.toResponse(patient)));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponse>> updateById(@PathVariable long id,
                                                      @RequestBody PatientRequest request) {
        Patient patient = this.patientPortIn.updateById(id, PatientRestMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(PatientRestMapper.toResponse(patient)));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        this.patientPortIn.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/dni/{dni}")
    public ResponseEntity<ApiResponse<PatientResponse>> getByDni(@PathVariable String dni) {
        Patient patient = this.patientPortIn.getByDni(dni);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(PatientRestMapper.toResponse(patient)));
    }

    @Override
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getByName(@PathVariable("nombre") String name) {
        List<Patient> patients = this.patientPortIn.getByName(name);
        List<PatientResponse> response = new ArrayList<>();
        for(Patient patient : patients) {
            response.add(PatientRestMapper.toResponse(patient));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(response));
    }

    @Override
    @GetMapping("/obra-social")
    public ResponseEntity<ApiResponse<List<PatientResponse>>> getByHealthInsuranceProvider(@RequestParam("obra_social") String healthInsuranceProvider,
                                                                              @RequestParam int page,
                                                                              @RequestParam int size) {
        List<Patient> patients = this.patientPortIn.getByHealthInsuranceProvider(healthInsuranceProvider,page,size);
        List<PatientResponse> response = new ArrayList<>();
        for(Patient patient : patients){
            response.add(PatientRestMapper.toResponse(patient));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(response));
    }

    @GetMapping("/exportar")
    @Override
    public ResponseEntity<ApiResponse<String>> export(@RequestParam("formato") String format) {
        String patientsExport = this.patientPortIn.exportPatients(format);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(patientsExport));
    }

}
