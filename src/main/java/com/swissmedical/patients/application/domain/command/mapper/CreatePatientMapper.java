package com.swissmedical.patients.application.domain.command.mapper;

import com.swissmedical.patients.application.domain.command.CreatePatientCommand;
import com.swissmedical.patients.application.domain.factory.PatientFactory;
import com.swissmedical.patients.application.domain.model.Patient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CreatePatientMapper {

    private final PatientFactory pacienteFactory;
    public Patient toPatient(CreatePatientCommand command) {
        return pacienteFactory.create(
                command.getId(),
                command.getFirstName(),
                command.getLastName(),
                command.getEmail(),
                command.getPhoneNumber(),
                command.getDni(),
                command.getMemberNumber(),
                command.getBirthDate(),
                command.isActive(),
                command.getSocialSecurity()
        );
    }

}
