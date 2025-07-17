package com.swissmedical.patients.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreateDto {

    @NotNull(message = "First name cannot be null")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;

    @NotNull(message = "DNI cannot be null")
    @Size(min = 8, max = 10, message = "DNI must be between 8 and 15 characters")
    private String dni;

    @NotNull(message = "Member number cannot be null")
    @Size(min = 5, max = 15, message = "Member number must be between 5 and 15 characters")
    private String memberNumber;

    @NotNull(message = "Birth date cannot be null")
    private String birthDate;

    private boolean isActive;

    @NotNull(message = "Social Security cannot be null")
    private String socialSecurity;
}
