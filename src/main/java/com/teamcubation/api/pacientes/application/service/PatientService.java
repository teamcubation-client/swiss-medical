package com.teamcubation.api.pacientes.application.service;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.domain.port.in.IPatientPortIn;
import com.teamcubation.api.pacientes.application.domain.port.out.IPatientPortOut;
import com.teamcubation.api.pacientes.shared.exception.DuplicatedPatientException;
import com.teamcubation.api.pacientes.shared.exception.PatientDniAlreadyInUse;
import com.teamcubation.api.pacientes.shared.exception.PatientNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements IPatientPortIn {

    private final IPatientPortOut iPatientPortOut;
    private static final String DNI_FIELD = "DNI";

    public PatientService(IPatientPortOut iPatientPortOut){
        this.iPatientPortOut = iPatientPortOut;
    }

    @Override
    @Transactional
    public Patient create(Patient patient) {
        Optional<Patient> patientFound = this.iPatientPortOut.findByDni(patient.getDni());

        if(patientFound.isPresent()){
            throw new DuplicatedPatientException(DNI_FIELD, patient.getDni());
        }
        return iPatientPortOut.save(patient);
    }

    @Override
    public List<Patient> getAll(String dni, String name) {
        return this.iPatientPortOut.findAll(dni, name);
    }

    @Override
    public Patient getById(long id) {
        return this.iPatientPortOut.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Override
    public Patient getByDni(String dni) {
        return this.iPatientPortOut.findByDni(dni)
                .orElseThrow(() -> new PatientNotFoundException(dni));
    }

    @Override
    public List<Patient> getByName(String name) {
        return this.iPatientPortOut.findByName(name);
    }

    @Override
    public List<Patient> getByHealthInsuranceProvider(String healthInsuranceProvider, int page, int size) {
        return this.iPatientPortOut.findByHealthInsuranceProvider(healthInsuranceProvider, size, page * size);
    }

    @Override
    @Transactional
    public Patient updateById(long id, Patient patientUpdated) {
        Patient patient = this.iPatientPortOut.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        if(patientUpdated.getDni() != null) {
            String dni = patientUpdated.getDni();
            Optional<Patient> patientDuplicatedDNI = this.iPatientPortOut.findByDni(dni);
            boolean alreadyInUse = patientDuplicatedDNI.isPresent() && !patientDuplicatedDNI.get().getId().equals(id);
            if (alreadyInUse) {
                throw new PatientDniAlreadyInUse(id);
            }
        }

        copyNotNull(patientUpdated, patient);
        return this.iPatientPortOut.save(patient);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        this.iPatientPortOut.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        this.iPatientPortOut.deleteById(id);
    }

    /**
     * Copia solo los campos no nulos desde el objeto de datos actualizados hacia el paciente existente.
     *
     * Este método permite realizar una actualización parcial del paciente,
     * manteniendo los valores originales de aquellos campos que no fueron modificados.
     *
     * Es útil en operaciones donde no se requiere sobrescribir todos los datos.
     *
     * @param updatedData Objeto que contiene los nuevos valores (puede tener campos nulos).
     * @param patient Entidad persistida a la que se le aplicarán los cambios NO nulos.
     */
    private void copyNotNull(Patient updatedData, Patient patient) {
        if (updatedData.getName() != null) patient.setName(updatedData.getName());
        if (updatedData.getLastName() != null) patient.setLastName(updatedData.getLastName());
        if (updatedData.getDni() != null) patient.setDni(updatedData.getDni());
        if (updatedData.getHealthInsuranceProvider() != null) patient.setHealthInsuranceProvider(updatedData.getHealthInsuranceProvider());
        if (updatedData.getEmail() != null) patient.setEmail(updatedData.getEmail());
        if (updatedData.getPhoneNumber() != null) patient.setPhoneNumber(updatedData.getPhoneNumber());
    }
}
