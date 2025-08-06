package com.tq.pacientes.application.domain.model.patient.processing;

import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.application.domain.model.Patient;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.tq.pacientes.shared.util.PatientConstants.RISK_HIGH;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_LOW;
import static com.tq.pacientes.shared.util.PatientConstants.RISK_MEDIUM;
import static com.tq.pacientes.shared.util.PatientConstants.SENIOR_AGE;
import static com.tq.pacientes.shared.util.PatientConstants.SENIOR_AGE_PLUS;

import static com.tq.pacientes.shared.util.PatientUtils.calculateAge;
import static org.springframework.util.StringUtils.capitalize;

@Component
@Getter
public class SeniorPatientSave extends PatientSaveTemplate {

    private int currentPatientAge;

    public SeniorPatientSave(PatientRepositoryPort patientRepositoryPort) {
        super(patientRepositoryPort);
    }

    @Override
    protected void validate(Patient patient) {
        currentPatientAge = calculateAge(patient);
    }

    @Override
    protected void preProcess(Patient patient) {
        patient.setFirstName(capitalize(patient.getFirstName()));
        patient.setLastName(capitalize(patient.getLastName()));
    }

    @Override
    protected void postProcess(Patient patient) {
    }

    public String assessGeriatricComplexity(int age) {
        if (age >= SENIOR_AGE_PLUS) return RISK_HIGH;
        if (age >= SENIOR_AGE) return RISK_MEDIUM;
        return RISK_LOW;
    }
}
