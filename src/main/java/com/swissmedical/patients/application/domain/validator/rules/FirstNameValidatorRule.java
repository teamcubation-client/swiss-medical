package com.swissmedical.patients.application.domain.validator.rules;

import static com.swissmedical.patients.shared.constants.Constants.FIRSTNAME_REGEX;
import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientInvalidException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FirstNameValidatorRule implements PatientValidatorRule {

    @Override
    public void validate(Patient patient) throws PatientInvalidException {
        boolean isInvalid = isInvalid(patient.getFirstName());
        if (isInvalid)
            throw new PatientInvalidException("First name is invalid");
    }

    private boolean isInvalid(String nombre) {
        return !nombre.matches(FIRSTNAME_REGEX);
    }
}
