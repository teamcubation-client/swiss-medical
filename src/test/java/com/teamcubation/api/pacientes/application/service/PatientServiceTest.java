package com.teamcubation.api.pacientes.application.service;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory.ExporterFactory;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory.ExporterFactoryProvider;
import com.teamcubation.api.pacientes.infrastructure.adapter.out.persistence.repository.PatientRepository;
import com.teamcubation.api.pacientes.shared.exception.DuplicatedPatientException;
import com.teamcubation.api.pacientes.shared.exception.ExporterTypeNotSupportedException;
import com.teamcubation.api.pacientes.shared.exception.PatientDniAlreadyInUseException;
import com.teamcubation.api.pacientes.shared.exception.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PatientServiceTest {

    private PatientService patientService;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private ExporterFactoryProvider exporterFactoryProvider;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.patientService = new PatientService(this.patientRepository, exporterFactoryProvider);
    }

    private Patient buildPatient(Long id, String name, String lastName, String dni, String healthInsuranceProvider, String email, String phoneNumber) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setLastName(lastName);
        patient.setDni(dni);
        patient.setHealthInsuranceProvider(healthInsuranceProvider);
        patient.setEmail(email);
        patient.setPhoneNumber(phoneNumber);
        return patient;
    }

    //TEST CREAR PACIENTE
    @Test
    void createPatient_ShouldReturnPatientCreatedWhitAllFieldsComplete() {
        Patient patient = buildPatient(null, "Ana", "Torres", "35784627",
                "Swiss Medical", "anatorres@gmail.com", "1144773898");

        Patient savedPatient = buildPatient(1L, "Ana", "Torres", "35784627",
                "Swiss Medical", "anatorres@gmail.com", "1144773898");

        when(this.patientRepository.save(patient)).thenReturn(savedPatient);

        Patient result = this.patientService.create(patient);
        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Ana", result.getName()),
                () -> assertEquals("Torres", result.getLastName()),
                () -> assertEquals("35784627", result.getDni()),
                () -> assertEquals("anatorres@gmail.com",result.getEmail()),
                () -> assertEquals("Swiss Medical",result.getHealthInsuranceProvider()),
                () -> assertEquals("1144773898",result.getPhoneNumber())
        );
        verify(this.patientRepository).save(patient);
    }

    @Test
    void createPatientWithRequiredFields_ShouldReturnPatientCreated(){
        Patient patient = buildPatient(null, "Ana", "Torres", "35784627",
                null, null, null);

        Patient savedPatient = buildPatient(1L, "Ana", "Torres", "35784627",
                null, null, null);

        when(this.patientRepository.save(patient)).thenReturn(savedPatient);

        Patient result = this.patientService.create(patient);
        assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Ana", result.getName()),
                () -> assertEquals("Torres", result.getLastName()),
                () -> assertEquals("35784627", result.getDni()),
                () -> assertNull(result.getEmail()),
                () -> assertNull(result.getHealthInsuranceProvider()),
                () -> assertNull(result.getPhoneNumber())
        );
        verify(this.patientRepository).save(patient);
    }

    @Test
    void createPatientWithDuplicateDni_ShouldThrowException() {
        Patient patient = buildPatient(null,null,null,"47837645",
                null,null,null);

        when(this.patientRepository.findByDni(patient.getDni()))
                .thenReturn(Optional.of(new Patient()));

        DuplicatedPatientException exception = assertThrows(DuplicatedPatientException.class,
                () -> { this.patientService.create(patient);}
        );

        assertTrue(exception.getMessage().contains("Ya existe un paciente con DNI: 47837645"));
        verify(this.patientRepository).findByDni(patient.getDni());
    }

    //TEST OBTENER PACIENTES
    @Test
    void getPatientsWithNullFilter_ShouldReturnAllPatients() {
        Patient p1 = buildPatient(1L, "Ana", "Torres", "35784627",
                null, null, null);
        Patient p2 = buildPatient(2L, "Juan", "Pérez", "47837645",
                null, null, null);
        Patient p3 = buildPatient(3L, "Lucía", "González", "56987412",
                null, null, null);

        List<Patient> patients = List.of(p1, p2, p3);

        when(this.patientRepository.findAll(null,null)).thenReturn(patients);
        List<Patient> result = this.patientService.getAll(null,null);
        assertEquals(patients,result);
        verify(this.patientRepository).findAll(null,null);
    }

    @Test
    void getPatientsWithEmptyFilter_ShouldReturnAllPatients() {
        Patient p1 = buildPatient(1L, "Ana", "Torres", "35784627",
                null, null, null);
        Patient p2 = buildPatient(2L, "Juan", "Pérez", "47837645",
                null, null, null);
        Patient p3 = buildPatient(3L, "Lucía", "González", "56987412",
                null, null, null);

        List<Patient> patients = List.of(p1, p2, p3);

        when(this.patientRepository.findAll("","")).thenReturn(patients);
        List<Patient> result = this.patientService.getAll("","");
        assertEquals(patients,result);
        verify(this.patientRepository).findAll("","");
    }

    @Test
    void getPatientsWithFullDniFilter_ShouldReturnPatientWithDni() {
        Patient patient = buildPatient(3L, "Lucía", "González", "23456789",
                null, null, null);

        List<Patient> patients = List.of(patient);

        when(this.patientRepository.findAll("23456789",null)).thenReturn(patients);
        List<Patient> result = this.patientService.getAll("23456789",null);

        assertFalse(result.isEmpty());
        assertAll(
                () -> result.forEach(
                        p -> assertEquals("23456789",p.getDni())
                )
        );
        verify(this.patientRepository).findAll("23456789",null);
    }

    @Test
    void getPatientsWithPartialDniFilter_ShouldReturnEmptyList() {
        when(this.patientRepository.findAll("2345",null)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll("2345",null);
        assertTrue(result.isEmpty());
        verify(this.patientRepository).findAll("2345",null);
    }

    @Test
    void getPatientsWithNonexistentDniFiltered_ShouldReturnEmptyList() {
        when(this.patientRepository.findAll("23456789",null)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll("23456789",null);
        assertTrue(result.isEmpty());
        verify(this.patientRepository).findAll("23456789",null);
    }

    @Test
    void getPatientsWithFullNameFilter_ShouldReturnMatchingPatients() {
        Patient juan = buildPatient(1L, "Juan", "Pérez", "12345678",
                null, null, null);
        Patient juan2 = buildPatient(2L, "juan", "García", "87654321",
                null, null, null);
        Patient juan3 = buildPatient(3L, "JUAN", "López", "23456789",
                null, null, null);

        List<Patient> patientsWithSameName = List.of(juan, juan2, juan3);

        when(this.patientRepository.findAll(null,"Juan")).thenReturn(patientsWithSameName);
        List<Patient> result = this.patientService.getAll(null,"Juan");
        assertAll(
                () -> assertEquals(patientsWithSameName.size(), result.size()),
                () -> result.forEach(
                        p -> assertTrue(p.getName().equalsIgnoreCase("Juan"))
                )
        );

        verify(this.patientRepository).findAll(null,"Juan");
    }

    @Test
    void getPatientsWithPartialNameFilter_ShouldReturnPatientsWithMatchFiltered() {
        Patient patient = buildPatient(1L, "Jorge", "Pérez", "12345678",
                null, null, null);
        Patient patient2 = buildPatient(2L, "jonathan", "García", "87654321",
                null, null, null);
        Patient patient3 = buildPatient(3L, "JOEL", "López", "23456789",
                null, null, null);

        List<Patient> patientsWithMatchName = List.of(patient, patient2, patient3);

        when(this.patientRepository.findAll(null,"Jo")).thenReturn(patientsWithMatchName);
        List<Patient> result = this.patientService.getAll(null,"Jo");

        assertAll(
                () -> assertEquals(patientsWithMatchName.size(), result.size()),
                () -> result.forEach(p -> assertTrue(p.getName().toLowerCase().startsWith("jo")))
        );
        verify(this.patientRepository).findAll(null, "Jo");
    }

    @Test
    void getPatientsWithNonexistentNameFiltered_ShouldReturnEmptyList() {
        when(this.patientRepository.findAll(null,"Yoiner")).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll(null, "Yoiner");
        assertTrue(result.isEmpty());
        verify(this.patientRepository).findAll(null, "Yoiner");
    }

    @Test
    void getPatientsWithFullDniAndPartialNameFiltered_ShouldReturnMatchingPatients() {
        Patient patient = buildPatient(2L, "Juan", "Pérez", "47837645",
                null, null, null);

        List<Patient> patients = List.of(patient);

        when(this.patientRepository.findAll("47837645","Ju")).thenReturn(patients);

        List<Patient> result = this.patientService.getAll("47837645","Ju");
        assertAll(
                () -> assertEquals(patients.size(), result.size()),
                () -> result.forEach(
                        p -> {
                            assertTrue(p.getName().equalsIgnoreCase("Juan"));
                            assertEquals(p.getDni(), "47837645");
                        }
                )
        );
        verify(this.patientRepository).findAll("47837645","Ju");
    }

    @Test
    void getPatientsWithNonexistentDniAndNameFiltered_ShouldReturnEmptyList() {
        when(this.patientRepository.findAll("39284750","Ana")).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll("39284750","Ana");
        assertTrue(result.isEmpty());
        verify(this.patientRepository).findAll("39284750","Ana");
    }

    //TEST OBTENER PACIENTES POR ID
    @Test
    void getPatientByExistentId_ShouldReturnPatientWithId() {
        Patient patient = buildPatient(1L, "Ana", "Torres", "35784627",
                "Swiss Medical", "ana.torres@example.com", "1144773898");
        when(this.patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = this.patientService.getById(1L);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Ana", result.getName()),
                () -> assertEquals("Torres", result.getLastName()),
                () -> assertEquals("35784627", result.getDni()),
                () -> assertEquals("Swiss Medical", result.getHealthInsuranceProvider()),
                () -> assertEquals("ana.torres@example.com", result.getEmail()),
                () -> assertEquals("1144773898", result.getPhoneNumber())
        );
        verify(this.patientRepository).findById(1L);
    }

    @Test
    void getPatientByNonExistentId_ShouldThrowException() {
        when(this.patientRepository.findById(5L)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> { this.patientService.getById(5L);}
        );
        assertTrue(exception.getMessage().contains("Paciente con ID: 5 no encontrado"));
        verify(this.patientRepository).findById(5L);
    }

    //TEST OBTENER PACIENTES POR DNI
    @Test
    void getPatientByExistentDni_ShouldReturnPatientWithDni() {
        Patient patient = buildPatient(1L, "Ana", "Torres", "35784627",
                "Swiss Medical", "ana.torres@example.com", "1144773898");
        when(this.patientRepository.findByDni("35784627")).thenReturn(Optional.of(patient));

        Patient result = this.patientService.getByDni("35784627");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Ana", result.getName()),
                () -> assertEquals("Torres", result.getLastName()),
                () -> assertEquals("35784627", result.getDni()),
                () -> assertEquals("Swiss Medical", result.getHealthInsuranceProvider()),
                () -> assertEquals("ana.torres@example.com", result.getEmail()),
                () -> assertEquals("1144773898", result.getPhoneNumber())
        );
        verify(this.patientRepository).findByDni("35784627");
    }

    @Test
    void getPatientByNonExistentDni_ShouldThrowException() {
        when(this.patientRepository.findByDni("35784627")).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> { this.patientService.getByDni("35784627");}
        );
        assertTrue(exception.getMessage().contains("Paciente con DNI: 35784627 no encontrado"));
        verify(this.patientRepository).findByDni("35784627");
    }

    //TEST OBTENER PACIENTE POR NOMBRE
    @Test
    void getPatientByExistentFullName_ShouldReturnPatientsWithMatchingName() {
        Patient patient = buildPatient(1L, "Florencia", "Martínez", "12345678",
                "OSDE", "flor.martinez@example.com", "1134567890");

        Patient patient2 = buildPatient(2L, "Florencia", "Gómez", "87654321",
                "Swiss Medical", "flor.gomez@example.com", "1198765432");

        Patient patient3 = buildPatient(3L, "Florencia", "López", "45678912",
                "Galeno", "flor.lopez@example.com", "1176543210");
        List<Patient> patients = List.of(patient,patient2,patient3);

        when(this.patientRepository.findByName("Florencia")).thenReturn(patients);

        List<Patient> result = this.patientService.getByName("Florencia");
        assertAll(
                () -> assertEquals(patients.size(), result.size()),
                () -> result.forEach(
                        p -> {
                            assertTrue(p.getName().equalsIgnoreCase("Florencia"));
                        }
                )
        );
        verify(this.patientRepository).findByName("Florencia");
    }

    @Test
    void getPatientByNonExistentFullName_ShouldReturnEmptyList() {
        when(this.patientRepository.findByName("Florencia")).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getByName("Florencia");
        assertTrue(result.isEmpty());
        verify(this.patientRepository).findByName("Florencia");
    }

    //TEST OBTENER PACIENTE POR OBRA SOCIAL
    @Test
    void getPatientByExistentHealthInsuranceProvider_ShouldReturnPagedList() {
        String healthInsuranceProvider = "Swiss Medical";
        int page = 1;
        int size = 5;

        Patient patient1 = buildPatient(1L, "Ana", "Torres", "35784627",
                healthInsuranceProvider, "ana.torres@example.com", "1144773898");
        Patient patient2 = buildPatient(2L, "Juan", "Pérez", "47837645",
                healthInsuranceProvider, "juan.perez@example.com", "1133557799");
        List<Patient> expectedPatients = List.of(patient1, patient2);

        when(this.patientRepository.findByHealthInsuranceProvider(healthInsuranceProvider, size, page * size))
                .thenReturn(expectedPatients);

        List<Patient> result = this.patientService.getByHealthInsuranceProvider(healthInsuranceProvider, page, size);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).getName());
        assertEquals("Swiss Medical", result.get(0).getHealthInsuranceProvider());

        verify(this.patientRepository).findByHealthInsuranceProvider(healthInsuranceProvider, size, page * size);
    }

    @Test
    void getPatientByNonexistentHealthInsuranceProvider_ShouldReturnEmptyList() {
        String healthInsuranceProvider = "Galeno";
        int page = 0;
        int size = 10;

        when(this.patientRepository.findByHealthInsuranceProvider(healthInsuranceProvider, size, 0))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getByHealthInsuranceProvider(healthInsuranceProvider, page, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(this.patientRepository).findByHealthInsuranceProvider(healthInsuranceProvider, size, 0);
    }

    @Test
    void getPatientByHealthInsuranceProviderWithExcessiveSizeSet_ShouldReturnMaxSizeSetAllowed() {
        String healthInsurance = "OSDE";
        int page = 999;
        int size = 10;

        when(this.patientRepository.findByHealthInsuranceProvider(healthInsurance, size, page * size))
                .thenReturn(Collections.emptyList());

        List<Patient> result = patientService.getByHealthInsuranceProvider(healthInsurance, page, size);

        assertTrue(result.isEmpty());
        verify(this.patientRepository).findByHealthInsuranceProvider(healthInsurance, size, page * size);
    }

    //TEST ACTUALIZAR POR ID
    @Test
    void updatePatientByValidIdWithFullUpdate_ShouldReplaceAllFields() {
        Long id = 1L;
        Patient existingPatient = buildPatient(id, "Juan", "Pérez", "39847562",
                "OSDE", "juan@mail.com", "1188749873");

        Patient updateRequest = buildPatient(id, "Carlos", "Gómez", "40234567",
                "Galeno", "carlos_gomez@mail.com", "1199999999");

        Patient expectedPatient = buildPatient(id, "Carlos", "Gómez", "40234567",
                "Galeno", "carlos_gomez@mail.com", "1199999999");

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));
        when(this.patientRepository.save(any(Patient.class))).thenReturn(expectedPatient);

        Patient result = this.patientService.updateById(id, updateRequest);

        ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class);
        verify(this.patientRepository).save(captor.capture());
        Patient savedPatient = captor.getValue();

        assertEquals(expectedPatient.getName(), savedPatient.getName());
        assertEquals(expectedPatient.getLastName(), savedPatient.getLastName());
        assertEquals(expectedPatient.getDni(), savedPatient.getDni());
        assertEquals(expectedPatient.getHealthInsuranceProvider(), savedPatient.getHealthInsuranceProvider());
        assertEquals(expectedPatient.getEmail(), savedPatient.getEmail());
        assertEquals(expectedPatient.getPhoneNumber(), savedPatient.getPhoneNumber());
        assertEquals(expectedPatient, result);
    }

    @Test
    void updatePatientByValidIdWithPartialUpdate_ShouldUpdateOnlyProvidedFields() {
        Long id = 1L;
        Patient existingPatient = buildPatient(id, "Juan", "Pérez", "39847562",
                "OSDE", "juan@mail.com", "1188749873");
        Patient updateRequest = buildPatient(id, null, null, null,
                "Swiss Medical", "juan_perez@gmail.com", null);

        Patient expectedPatient = buildPatient(id, "Juan", "Pérez", "39847562",
                "Swiss Medical", "juan_perez@gmail.com", "1188749873");


        when(this.patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));
        when(this.patientRepository.save(any(Patient.class))).thenReturn(expectedPatient);

        Patient result = this.patientService.updateById(id, updateRequest);

        ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class); //Tuve que usarlo porque el metodo save devuelve otra instancia del objeto Patient
        verify(this.patientRepository).save(captor.capture());
        Patient savedPatient = captor.getValue();

        assertEquals(expectedPatient.getName(), savedPatient.getName());
        assertEquals(expectedPatient.getLastName(), savedPatient.getLastName());
        assertEquals(expectedPatient.getDni(), savedPatient.getDni());
        assertEquals(expectedPatient.getHealthInsuranceProvider(), savedPatient.getHealthInsuranceProvider());
        assertEquals(expectedPatient.getEmail(), savedPatient.getEmail());
        assertEquals(expectedPatient.getPhoneNumber(), savedPatient.getPhoneNumber());
        assertEquals(expectedPatient, result);
    }

    @Test
    void updatePatientByIdButNotFound_ShouldThrowPatientNotFoundException() {
        Long id = 1L;
        Patient updateRequest = buildPatient(id, "Carlos", "Gómez", "40234567",
                "Galeno", "carlos_gomez@mail.com", "1199999999");

        when(this.patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> this.patientService.updateById(id, updateRequest));

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository, never()).save(any());
    }

    @Test
    void updatePatientByIdWithDuplicateDni_ShouldThrowPatientDniAlreadyInUse() {
        Long id = 1L;

        Patient existingPatient = buildPatient(id, "Juan", "Pérez", "39847562",
                "OSDE", "juan@mail.com", "1188749873");

        Patient otherPatient = buildPatient(2L, "Ana", "López", "40234567",
                "Swiss Medical", "ana_lopez@mail.com", "1177777777");

        // El request intenta cambiar a un DNI que ya usa otro paciente
        Patient updateRequest = buildPatient(id, "Juan", "Pérez", "40234567",
                "OSDE", "juan_perez@mail.com", "1188749873");

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));
        when(this.patientRepository.findByDni(updateRequest.getDni()))
                .thenReturn(Optional.of(otherPatient));

        assertThrows(PatientDniAlreadyInUseException.class,
                () -> this.patientService.updateById(id, updateRequest));

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository).findByDni(updateRequest.getDni());
        verify(this.patientRepository, never()).save(any());
    }

    //TEST BORRAR POR ID
    @Test
    void deleteByValidId_ShouldDeletePatient() {
        Long id = 1L;
        Patient existingPatient = new Patient(); // o usar buildPatient si tenés helper
        existingPatient.setId(id);

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));

        patientService.deleteById(id);

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository).deleteById(id);
    }

    @Test
    void deleteByNonExistentId_ShouldThrowPatientNotFoundException() {
        Long id = 1L;

        when(this.patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> patientService.deleteById(id));

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository, never()).deleteById(anyLong());
    }

    //TEST EXPORTAR LISTA PACIENTES
    @Test
    void exportPatients_ShouldReturnExportedData() {
        String format = "csv";
        Patient patient1 = buildPatient(13L, "Roberto", "Torres", "58473659",
                "SwissMedical", "roberto.torres@example.com", "1188749873");
        List<Patient> patients = List.of(patient1);

        when(this.patientRepository.findAll(null, null)).thenReturn(patients);

        ExporterFactory factory = mock(ExporterFactory.class);
        PatientExporterPortOut exporter = mock(PatientExporterPortOut.class);
        String exportedData = "id,nombre,apellido,dni,obra_social,email,telefono\n13,Roberto,Torres,58473659,SwissMedical,roberto.torres@example.com,1123556766";

        when(this.exporterFactoryProvider.getFactory(format)).thenReturn(factory);
        when(factory.createPatientExporter()).thenReturn(exporter);
        when(exporter.export(patients)).thenReturn(exportedData);

        String result = patientService.exportPatients(format);

        assertEquals(exportedData, result);
        verify(this.patientRepository).findAll(null, null);
        verify(this.exporterFactoryProvider).getFactory(format);
        verify(factory).createPatientExporter();
        verify(exporter).export(patients);
    }

    @Test
    void exportPatientsWithEmptyList_ShouldStillCallExporter() {
        String format = "csv";
        List<Patient> patients = new ArrayList<>();

        when(this.patientRepository.findAll(null, null)).thenReturn(patients);
        ExporterFactory factory = mock(ExporterFactory.class);
        PatientExporterPortOut exporter = mock(PatientExporterPortOut.class);

        String exportedData = "id,nombre,apellido,dni,obra_social,email,telefono\n"; // solo header, nadathis. más

        when(this.exporterFactoryProvider.getFactory(format)).thenReturn(factory);
        when(factory.createPatientExporter()).thenReturn(exporter);
        when(exporter.export(patients)).thenReturn(exportedData);

        String result = patientService.exportPatients(format);

        assertEquals(exportedData, result);

        verify(this.patientRepository).findAll(null, null);
        verify(this.exporterFactoryProvider).getFactory(format);
        verify(factory).createPatientExporter();
        verify(exporter).export(patients);
    }

    @Test
    void exportPatientsWithNotSupportedFormat_ShouldThrowException() {
        String invalidFormat = "jpg";

        when(this.patientRepository.findAll(null, null)).thenReturn(List.of());
        when(this.exporterFactoryProvider.getFactory(invalidFormat))
                .thenThrow(new ExporterTypeNotSupportedException(invalidFormat));

        ExporterTypeNotSupportedException exception = assertThrows(ExporterTypeNotSupportedException.class,
                () -> this.patientService.exportPatients(invalidFormat)
        );

        assertTrue(exception.getMessage().contains("Tipo de exportación ingresado 'jpg' no soportado"));
        verify(this.patientRepository).findAll(null,null);
        verify(this.exporterFactoryProvider).getFactory(invalidFormat);
    }
}