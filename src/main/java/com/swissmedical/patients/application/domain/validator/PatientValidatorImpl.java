package com.swissmedical.patients.application.domain.validator;

import com.swissmedical.patients.application.domain.model.*;
import com.swissmedical.patients.application.domain.validator.rules.*;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PatientValidatorImpl implements PatientValidator {
    private List<PatientValidatorRule> rules;

    @Override
    public void validate(Patient patient) {
        rules.forEach(rule -> rule.validate(patient));
    }
}