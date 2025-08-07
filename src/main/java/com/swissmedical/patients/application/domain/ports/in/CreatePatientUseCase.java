package com.swissmedical.patients.application.domain.ports.in;

import com.swissmedical.patients.application.domain.command.CreatePatientCommand;
import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;

public interface CreatePatientUseCase {

  Patient create(CreatePatientCommand command) throws PatientNotFoundException;

}
