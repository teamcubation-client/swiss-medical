package com.swissmedical.patients.application.domain.validator;

import com.swissmedical.patients.application.domain.model.*;

public interface PatientValidator {
    void validate(Patient patient);
}
