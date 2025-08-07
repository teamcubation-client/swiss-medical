package com.swissmedical.patients.application.domain.validator.rules;

import com.swissmedical.patients.application.domain.model.Patient;
import com.swissmedical.patients.shared.exceptions.PatientInvalidException;
import lombok.AllArgsConstructor;

import static com.swissmedical.patients.shared.constants.Constants.PHONENUMBER_REGEX;

@AllArgsConstructor
public class PhoneNumberValidatorRule implements PatientValidatorRule {

    @Override
    public void validate(Patient patient) throws PatientInvalidException {
        boolean isInvalid = isInvalid(patient.getPhoneNumber());
        if (isInvalid)
            throw new PatientInvalidException("Phone number is invalid");
    }

    private boolean isInvalid(String telefono) {
        return !telefono.matches(PHONENUMBER_REGEX);
    }
}
