package com.tq.pacientes.infrastructure.adapter.in.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientRequest {
    private String firstName;
    private String lastName;
    private String dni;
    private String healthInsurance;
    private String healthPlan;
    private String address;
    private String phoneNumber;
    private String email;
} 