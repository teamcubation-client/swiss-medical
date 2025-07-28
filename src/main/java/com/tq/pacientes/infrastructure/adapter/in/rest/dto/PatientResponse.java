package com.tq.pacientes.infrastructure.adapter.in.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private LocalDate birthDate;
    private String healthInsurance;
    private String healthPlan;
    private String address;
    private String phoneNumber;
    private String email;
    private Boolean active;
} 