package com.swissmedical.patients.application.domain.validator.rules;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientInvalidException;
import lombok.AllArgsConstructor;

import static com.swissmedical.patients.shared.constants.Constants.DNI_REGEX;

@AllArgsConstructor
public class DniValidatorRule implements PatientValidatorRule {

    @Override
    public void validate(Patient patient) throws PatientInvalidException {
        boolean isInvalid = isInvalid(patient.getDni());
        if (isInvalid)
            throw new PatientInvalidException("DNI is invalid");
    }

    private boolean isInvalid(String dni) {
        return !dni.matches(DNI_REGEX);
    }
}
