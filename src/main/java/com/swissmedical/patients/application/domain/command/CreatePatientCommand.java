package com.swissmedical.patients.application.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CreatePatientCommand {
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

    public CreatePatientCommand(String firstName, String lastName, String email, String phoneNumber, String dni, String memberNumber, LocalDate birthDate, boolean isActive, String socialSecurity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dni = dni;
        this.memberNumber = memberNumber;
        this.birthDate = birthDate;
        this.isActive = isActive;
        this.socialSecurity = socialSecurity;
    }
}
