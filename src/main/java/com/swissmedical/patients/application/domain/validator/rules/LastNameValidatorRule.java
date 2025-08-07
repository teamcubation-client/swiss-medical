package com.swissmedical.patients.application.domain.validator.rules;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientInvalidException;
import lombok.AllArgsConstructor;

import static com.swissmedical.patients.shared.constants.Constants.LASTNAME_REGEX;

@AllArgsConstructor
public class LastNameValidatorRule implements PatientValidatorRule {

    @Override
    public void validate(Patient patient) throws PatientInvalidException {
        boolean isInvalid = isInvalid(patient.getLastName());
        if (isInvalid)
            throw new PatientInvalidException("Last name is invalid");
    }

    private boolean isInvalid(String apellido) {
        return !apellido.matches(LASTNAME_REGEX);
    }
}
