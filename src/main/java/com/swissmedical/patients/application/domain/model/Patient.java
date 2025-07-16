package com.swissmedical.patients.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String dni;
  private String memberNumber;
  private LocalDate birthDate;
  private boolean isActive;
  private String socialSecurity;
}
