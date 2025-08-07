package com.swissmedical.patients.application.domain.validator.rules;

import com.swissmedical.patients.application.domain.model.*;
import com.swissmedical.patients.shared.exceptions.*;

public interface PatientValidatorRule {
    void validate(Patient patient) throws PatientInvalidException;
}
