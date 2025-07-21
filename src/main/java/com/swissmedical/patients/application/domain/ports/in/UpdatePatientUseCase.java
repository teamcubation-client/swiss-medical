package com.swissmedical.patients.application.domain.ports.in;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;

public interface UpdatePatientUseCase {

  Patient update(Long id, Patient patientDetails) throws PatientNotFoundException;

}
