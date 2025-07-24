package com.tq.pacientes.application.domain.model.patientprocessing;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.PatientType;
import com.tq.pacientes.application.domain.port.out.PatientRepositoryPort;
import com.tq.pacientes.application.service.PatientClassifier;
import org.springframework.stereotype.Component;

@Component
public class PatientSaveFactory {

    private final PatientClassifier classifier;

    public PatientSaveFactory(PatientClassifier classifier) {
        this.classifier = classifier;
    }

    public PatientSaveTemplate getPatientSaveStrategy(Patient patient, PatientRepositoryPort patientRepositoryPort) {
        PatientType type = classifier.classify(patient);

        return switch (type) {
            case SENIOR -> new SeniorPatientSave(patientRepositoryPort);
            case ADULT -> new AdultPatientSave(patientRepositoryPort);
            case YOUNG -> new YoungPatientSave(patientRepositoryPort);
        };
    }
}

