package com.swissmedical.patients.application.domain.ports.in;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientNotFoundException;

import java.util.List;

public interface ReadPatientUseCase {

  List<Patient> getAll(String name, int page, int size) throws IllegalArgumentException, PatientNotFoundException;

  Patient getByDni(String dni) throws PatientNotFoundException;

  List<Patient> getBySocialSecurity(String socialSecurity, int page, int size) throws PatientNotFoundException;
}
