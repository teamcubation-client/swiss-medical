package com.tq.pacientes.application.domain.model.patient.processing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.PatientType;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.application.service.PatientClassifier;
import org.springframework.stereotype.Component;

@Component
public class PatientSaveFactory {

    private final YoungPatientSave youngPatientSave;
    private final AdultPatientSave adultPatientSave;
    private final SeniorPatientSave seniorPatientSave;

    private final PatientClassifier classifier;

    public PatientSaveFactory(PatientClassifier classifier, YoungPatientSave youngPatientSave, AdultPatientSave adultPatientSave, SeniorPatientSave seniorPatientSave) {
        this.classifier = classifier;
        this.youngPatientSave = youngPatientSave;
        this.adultPatientSave = adultPatientSave;
        this.seniorPatientSave = seniorPatientSave;
    }

    public PatientSaveTemplate createStrategyFor(Patient patient, PatientRepositoryPort patientRepositoryPort) {
        if (patient == null || patientRepositoryPort == null) {
            throw new IllegalArgumentException("Patient and PatientRepositoryPort must not be null");
        }
        PatientType type = classifier.classify(patient);

        return switch (type) {
            case SENIOR -> seniorPatientSave;
            case ADULT -> adultPatientSave;
            case YOUNG -> youngPatientSave;
        };
    }
}

