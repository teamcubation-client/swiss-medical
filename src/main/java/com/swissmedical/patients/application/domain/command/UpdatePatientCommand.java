package com.swissmedical.patients.application.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePatientCommand {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dni;
    private String socialSecurity;
}