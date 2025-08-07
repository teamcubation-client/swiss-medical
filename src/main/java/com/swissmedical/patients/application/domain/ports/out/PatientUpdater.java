package com.swissmedical.patients.application.domain.ports.out;

import com.swissmedical.patients.application.domain.command.UpdatePatientCommand;
import com.swissmedical.patients.application.domain.model.Patient;

public interface PatientUpdater {
    void update(UpdatePatientCommand command, Patient patient);
}
