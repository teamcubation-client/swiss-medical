package com.swissmedical.patients.infrastructure.application;

import com.swissmedical.patients.application.domain.validator.PatientValidator;
import com.swissmedical.patients.application.domain.validator.PatientValidatorImpl;
import com.swissmedical.patients.application.domain.validator.rules.DniValidatorRule;
import com.swissmedical.patients.application.domain.validator.rules.EmailValidatorRule;
import com.swissmedical.patients.application.domain.validator.rules.FirstNameValidatorRule;
import com.swissmedical.patients.application.domain.validator.rules.LastNameValidatorRule;
import com.swissmedical.patients.application.domain.validator.rules.PhoneNumberValidatorRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public PatientValidator createPatientValidator() {
        return new PatientValidatorImpl(
                List.of(
                        new LastNameValidatorRule(),
                        new DniValidatorRule(),
                        new EmailValidatorRule(),
                        new FirstNameValidatorRule(),
                        new PhoneNumberValidatorRule()
                )
        );
    }
}
