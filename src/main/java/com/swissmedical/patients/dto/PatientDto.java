package com.swissmedical.patients.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

    @NotNull(message = "First name cannot be null")
    public String firstName;

    @NotNull(message = "Last name cannot be null")
    public String lastName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    public String email;

    @NotNull(message = "Phone number cannot be null")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    public String phoneNumber;

    @NotNull(message = "DNI cannot be null")
    @Size(min = 8, max = 10, message = "DNI must be between 8 and 15 characters")
    public String dni;

    @NotNull(message = "Member number cannot be null")
    @Size(min = 5, max = 15, message = "Member number must be between 5 and 15 characters")
    public String memberNumber;

    @NotNull(message = "Birth date cannot be null")
    public String birthDate;

    public boolean isActive;

    @NotNull(message = "Social Security cannot be null")
    public String socialSecurity;
}
