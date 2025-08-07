package com.swissmedical.patients.infrastructure.adapter.out.persistence.mysql.updater;

import com.swissmedical.patients.application.domain.command.UpdatePatientCommand;
import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.application.domain.ports.out.PatientUpdater;
import com.swissmedical.patients.application.domain.validator.PatientValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PatientUpdaterImpl implements PatientUpdater {
    private final MapStructPatientUpdater mapStructPatientUpdater;
    private final PatientValidator patientValidator;

    @Override
    public void update(UpdatePatientCommand command, Patient patient) {
        mapStructPatientUpdater.update(command, patient);
        patientValidator.validate(patient);
    }
}
