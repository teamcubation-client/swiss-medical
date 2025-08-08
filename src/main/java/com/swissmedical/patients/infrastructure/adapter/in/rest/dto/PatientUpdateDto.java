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
public class PatientUpdateDto {
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String dni;
  private String memberNumber;
  private String birthDate;
  private Boolean isActive;
  private String socialSecurity;
}
