package com.swissmedical.patients.infrastructure.adapter.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public String dni;
    public String memberNumber;
    public String birthDate;
    public boolean isActive;
    public String socialSecurity;

}
