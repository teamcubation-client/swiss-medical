package com.swissmedical.patients.application.domain.validator.rules;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientInvalidException;
import lombok.AllArgsConstructor;

import static com.swissmedical.patients.shared.constants.Constants.EMAIL_REGEX;

@AllArgsConstructor
public class EmailValidatorRule implements PatientValidatorRule {

    @Override
    public void validate(Patient patient) throws PatientInvalidException {
        boolean isInvalid = isInvalid(patient.getEmail());
        if (isInvalid)
            throw new PatientInvalidException("Email is invalid");
    }

    private boolean isInvalid(String email) {
        return !email.matches(EMAIL_REGEX);
    }
}
