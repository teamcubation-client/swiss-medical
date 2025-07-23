package com.swissmedical.patients.application.domain.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
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

  private Patient(PatientBuilder builder) {
    this.id = builder.id;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.email = builder.email;
    this.phoneNumber = builder.phoneNumber;
    this.dni = builder.dni;
    this.memberNumber = builder.memberNumber;
    this.birthDate = builder.birthDate;
    this.isActive = builder.isActive;
    this.socialSecurity = builder.socialSecurity;
  }

  public static PatientBuilder builder() {
    return new PatientBuilder();
  }

  public static class PatientBuilder {
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

    private PatientBuilder() {
    }

    public PatientBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public PatientBuilder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public PatientBuilder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public PatientBuilder email(String email) {
      this.email = email;
      return this;
    }

    public PatientBuilder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public PatientBuilder dni(String dni) {
      this.dni = dni;
      return this;
    }

    public PatientBuilder memberNumber(String memberNumber) {
      this.memberNumber = memberNumber;
      return this;
    }

    public PatientBuilder birthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
      return this;
    }

    public PatientBuilder isActive(boolean isActive) {
      this.isActive = isActive;
      return this;
    }

    public PatientBuilder socialSecurity(String socialSecurity) {
      this.socialSecurity = socialSecurity;
      return this;
    }

    public Patient build() {
      return new Patient(this);
    }
  }
}
