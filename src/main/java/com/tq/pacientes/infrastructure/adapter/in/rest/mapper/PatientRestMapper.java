package com.tq.pacientes.infrastructure.adapter.in.rest.mapper;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientRequest;
import com.tq.pacientes.infrastructure.adapter.in.rest.dto.PatientResponse;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class PatientRestMapper {
    public Patient toDomain(PatientRequest request) {
        return Patient.builder()
                .id(null)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dni(request.getDni())
                .healthInsurance(request.getHealthInsurance())
                .healthPlan(request.getHealthPlan())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .active(true)
                .creationDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    public PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dni(patient.getDni())
                .healthInsurance(patient.getHealthInsurance())
                .healthPlan(patient.getHealthPlan())
                .address(patient.getAddress())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .active(patient.getActive())
                .build();
    }
} 