package com.swissmedical.patients.application.domain.ports.in;

import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;

public interface DeletePatientUseCase {

  void delete(Long id) throws PatientNotFoundException;

}
