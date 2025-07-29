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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PatientServiceTest {

    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ExporterFactoryProvider exporterFactoryProvider;

    @Mock
    private ExporterFactory exporterFactory;

    @Mock
    private PatientExporterPortOut patientExporter;

    @BeforeEach
    public void setUp() {
        this.patientService = new PatientService(this.patientRepository, exporterFactoryProvider);
    }

    //Se puede usar para utilizar paciente por defecto o crear nuevos pacientes
    static class PatientBuilder {
        private Long id;
        private String name = "Ana";
        private String lastName = "Torres";
        private String dni = "35784627";
        private String insurance = null;
        private String email = null;
        private String phone = null;

        PatientBuilder withId(Long id) { this.id = id; return this; }
        PatientBuilder withName(String name) { this.name = name; return this; }
        PatientBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
        PatientBuilder withDni(String dni) { this.dni = dni; return this; }
        PatientBuilder withInsurance(String insurance) { this.insurance = insurance; return this; }
        PatientBuilder withEmail(String email) { this.email = email; return this; }
        PatientBuilder withPhone(String phone) { this.phone = phone; return this; }

        Patient build() {
            Patient p = new Patient();
            p.setId(id);
            p.setName(name);
            p.setLastName(lastName);
            p.setDni(dni);
            p.setHealthInsuranceProvider(insurance);
            p.setEmail(email);
            p.setPhoneNumber(phone);
            return p;
        }
    }

    private List<Patient> defaultPatients() {
        return List.of(
                new PatientBuilder().withId(1L).build(),
                new PatientBuilder().withId(2L)
                        .withName("Juan")
                        .withLastName("Pérez")
                        .withDni("47837645")
                        .build()
        );
    }

    private void assertPatientEquals(Patient expected, Patient actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId(),
                        "El ID del paciente no coincide"),
                () -> assertEquals(expected.getName(), actual.getName(),
                        "El nombre del paciente no coincide"),
                () -> assertEquals(expected.getLastName(), actual.getLastName(),
                        "El apellido del paciente no coincide"),
                () -> assertEquals(expected.getDni(), actual.getDni(),
                        "El DNI del paciente no coincide"),
                () -> assertEquals(expected.getEmail(), actual.getEmail(),
                        "El email del paciente no coincide"),
                () -> assertEquals(expected.getHealthInsuranceProvider(), actual.getHealthInsuranceProvider(),
                        "La obra social del paciente no coincide"),
                () -> assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber(),
                        "El teléfono del paciente no coincide")
        );
    }

    //TEST CREAR PACIENTE
    @Test
    void createPatient_ShouldReturnPatientCreatedWithAllFieldsComplete() {
        Patient toCreate = new PatientBuilder()
                .withEmail("anatorres@gmail.com")
                .withPhone("1144773898")
                .withInsurance("Swiss Medical")
                .build();
        Patient saved = new PatientBuilder()
                .withId(1L)
                .withEmail("anatorres@gmail.com")
                .withPhone("1144773898")
                .withInsurance("Swiss Medical")
                .build();

        when(this.patientRepository.save(toCreate)).thenReturn(saved);

        Patient result = this.patientService.create(toCreate);
        assertPatientEquals(saved, result);
        verify(this.patientRepository).save(toCreate);
    }

    @Test
    void createPatientWithRequiredFields_ShouldReturnPatientCreated(){
        Patient patient = new PatientBuilder().build();
        Patient savedPatient = new PatientBuilder().withId(1L).build();

        when(this.patientRepository.save(patient)).thenReturn(savedPatient);

        Patient result = this.patientService.create(patient);
        assertPatientEquals(savedPatient, result);
        verify(this.patientRepository).save(patient);
    }

    @Test
    void createPatientWithDuplicateDni_ShouldThrowException() {
        Patient patient = new PatientBuilder().build();

        when(this.patientRepository.findByDni(patient.getDni()))
                .thenReturn(Optional.of(new Patient()));

        DuplicatedPatientException exception = assertThrows(DuplicatedPatientException.class,
                () -> this.patientService.create(patient)
        );

        assertTrue(exception.getMessage().contains("Ya existe un paciente con DNI: " + patient.getDni()),
                "El mensaje de la excepción no coincide");
        verify(this.patientRepository).findByDni(patient.getDni());
    }

    //TEST OBTENER PACIENTES
    @Test
    void getPatientsWithNullFilter_ShouldReturnAllPatients() {
        List<Patient> patients = defaultPatients();

        when(this.patientRepository.findAll(null, null)).thenReturn(patients);

        List<Patient> result = this.patientService.getAll(null, null);

        assertEquals(patients, result, "La lista devuelta debe coincidir con la lista esperada de pacientes");
        verify(this.patientRepository).findAll(null, null);
    }

    @Test
    void getPatientsWithEmptyFilter_ShouldReturnAllPatients() {
        List<Patient> patients = defaultPatients();

        when(this.patientRepository.findAll("","")).thenReturn(patients);

        List<Patient> result = this.patientService.getAll("","");
        assertEquals(patients, result,
                "La lista devuelta debe coincidir exactamente con la lista esperada de pacientes");
        verify(this.patientRepository).findAll("","");
    }

    @Test
    void getPatientsWithNoResults_ShouldReturnEmptyList() {
        String dni = "34567898";
        when(this.patientRepository.findAll(dni, null)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll(dni, null);

        assertTrue(result.isEmpty(),
                "La lista devuelta debe estar vacía cuando no hay pacientes que coincidan con el filtro");
        verify(this.patientRepository).findAll(dni, null);
    }

    @Test
    void getPatientsWithDniFilter_ShouldReturnOnePatientWithDni() {
        Patient patient = new PatientBuilder().build();
        List<Patient> patients = List.of(patient);
        String dni = patient.getDni();

        when(this.patientRepository.findAll(dni,null)).thenReturn(patients);
        List<Patient> result = this.patientService.getAll(dni,null);

        assertEquals(1, result.size(),
                "Debe devolverse exactamente un paciente con el DNI: " + dni);

        assertTrue(result.stream().allMatch(p -> dni.equals(p.getDni())),
                "El paciente devuelto debe tener el DNI esperado: " + dni);

        assertPatientEquals(patient, result.get(0));
        verify(this.patientRepository).findAll(dni, null);
    }

    @Test
    void getPatientsWithNonexistentDniFiltered_ShouldReturnEmptyList() {
        String nonExistent = "23456789";
        when(this.patientRepository.findAll(nonExistent,null)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll(nonExistent,null);

        assertTrue(result.isEmpty(),
                "Cuando no existen pacientes con el DNI especificado debe devolver una lista vacía");
        verify(this.patientRepository).findAll(nonExistent,null);
    }

    @Test
    void getPatientsWithFullNameFilter_ShouldReturnMatchingPatients() {
        Patient juan1 = new PatientBuilder().withId(1L).withName("Juan").withLastName("Pérez").withDni("12345678").build();
        Patient juan2 = new PatientBuilder().withId(2L).withName("juan").withLastName("García").withDni("87654321").build();
        Patient juan3 = new PatientBuilder().withId(3L).withName("JUAN").withLastName("López").withDni("23456789").build();

        List<Patient> patientsWithSameName = List.of(juan1, juan2, juan3);

        when(this.patientRepository.findAll(null, "Juan")).thenReturn(patientsWithSameName);

        List<Patient> result = this.patientService.getAll(null, "Juan");

        assertEquals(patientsWithSameName.size(), result.size(),
                "El número de pacientes devueltos no coincide con la lista esperada");

        assertTrue(result.stream().allMatch(p -> p.getName().equalsIgnoreCase("Juan")),
                "Todos los pacientes devueltos deben tener nombre 'Juan', ignorando mayúsculas");

        verify(this.patientRepository).findAll(null, "Juan");
    }

    @Test
    void getPatientsWithPartialNameFilter_ShouldReturnPatientsWithMatchFiltered() {
        Patient patient1 = new PatientBuilder()
                .withId(1L).withName("Jorge").withLastName("Pérez").withDni("12345678").build();
        Patient patient2 = new PatientBuilder()
                .withId(2L).withName("jonathan").withLastName("García").withDni("87654321").build();
        Patient patient3 = new PatientBuilder()
                .withId(3L).withName("JOEL").withLastName("López").withDni("23456789").build();

        List<Patient> patientsWithMatchName = List.of(patient1, patient2, patient3);
        String nameFilter = "Jo";

        when(this.patientRepository.findAll(null, nameFilter)).thenReturn(patientsWithMatchName);

        List<Patient> result = this.patientService.getAll(null, nameFilter);

        assertEquals(patientsWithMatchName.size(), result.size(),
                "La cantidad de pacientes devuelta no coincide con la esperada para el filtro: " + nameFilter);

        assertTrue(result.stream().allMatch(
                        p -> p.getName().toLowerCase().startsWith(nameFilter.toLowerCase())),
                "Todos los pacientes devueltos deberían tener un nombre que comience con: " + nameFilter);

        verify(this.patientRepository).findAll(null, nameFilter);
    }

    @Test
    void getPatientsWithNonexistentNameFiltered_ShouldReturnEmptyList() {
        String nameFilter = "Yoiner";

        when(this.patientRepository.findAll(null, nameFilter))
                .thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll(null, nameFilter);

        assertTrue(result.isEmpty(),
                "La lista debería estar vacía porque no existen pacientes con el nombre: " + nameFilter);

        verify(this.patientRepository).findAll(null, nameFilter);
    }

    @Test
    void getPatientsWithFullDniAndPartialNameFiltered_ShouldReturnMatchingPatients() {
        String dniFilter = "47837645";
        String nameFilter = "Ju";

        Patient expectedPatient = new PatientBuilder()
                .withId(2L)
                .withName("Juan")
                .withLastName("Pérez")
                .withDni(dniFilter)
                .build();

        List<Patient> patients = List.of(expectedPatient);

        when(this.patientRepository.findAll(dniFilter, nameFilter)).thenReturn(patients);

        List<Patient> result = this.patientService.getAll(dniFilter, nameFilter);

        assertEquals(1, result.size(),
                "Se esperaba exactamente un paciente para el filtro DNI: " + dniFilter + " y nombre: " + nameFilter);

        Patient actual = result.get(0);
        assertTrue(actual.getName().equalsIgnoreCase("Juan"),
                "El nombre del paciente no coincide con el esperado (Juan) para el filtro: " + nameFilter);
        assertEquals(dniFilter, actual.getDni(),
                "El DNI del paciente no coincide con el esperado: " + dniFilter);

        assertPatientEquals(expectedPatient, actual);

        verify(this.patientRepository).findAll(dniFilter, nameFilter);
    }

    @Test
    void getPatientsWithNonexistentDniAndNameFiltered_ShouldReturnEmptyList() {
        String dni = "39284750";
        String name = "Ana";
        when(this.patientRepository.findAll(dni,name)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getAll(dni,name);
        assertTrue(result.isEmpty());
        verify(this.patientRepository).findAll(dni,name);
    }

    //TEST OBTENER PACIENTES POR ID
    @Test
    void getPatientByExistentId_ShouldReturnPatientWithId() {
        Long id = 1L;

        Patient expectedPatient = new PatientBuilder()
                .withId(id)
                .withName("Ana")
                .withLastName("Torres")
                .withDni("35784627")
                .withInsurance("Swiss Medical")
                .withEmail("ana.torres@example.com")
                .withPhone("1144773898")
                .build();

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(expectedPatient));

        Patient result = this.patientService.getById(id);

        assertNotNull(result, "El paciente no debería ser nulo para el ID: " + id);
        assertPatientEquals(expectedPatient, result);

        verify(this.patientRepository).findById(id);
    }

    @Test
    void getPatientByNonExistentId_ShouldThrowException() {
        long id = 5L;
        when(this.patientRepository.findById(id)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> { this.patientService.getById(id);}
        );
        assertTrue(exception.getMessage().contains("Paciente con ID: " + id + " no encontrado"));
        verify(this.patientRepository).findById(id);
    }

    //TEST OBTENER PACIENTES POR DNI
    @Test
    void getPatientByExistentDni_ShouldReturnPatientWithDni() {
        String dni = "35784627";

        Patient expectedPatient = new PatientBuilder()
                .withId(1L)
                .withName("Ana")
                .withLastName("Torres")
                .withDni(dni)
                .withInsurance("Swiss Medical")
                .withEmail("ana.torres@example.com")
                .withPhone("1144773898")
                .build();

        when(this.patientRepository.findByDni(dni)).thenReturn(Optional.of(expectedPatient));

        Patient result = this.patientService.getByDni(dni);

        assertNotNull(result, "El paciente no debería ser nulo para el DNI: " + dni);
        assertPatientEquals(expectedPatient, result);

        verify(this.patientRepository).findByDni(dni);
    }

    @Test
    void getPatientByNonExistentDni_ShouldThrowException() {
        String dni = "35784627";

        when(this.patientRepository.findByDni(dni)).thenReturn(Optional.empty());

        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> this.patientService.getByDni(dni)
        );

        assertTrue(exception.getMessage().contains("Paciente con DNI: " + dni + " no encontrado"),
                "El mensaje de la excepción no coincide");

        verify(this.patientRepository).findByDni(dni);
    }

    //TEST OBTENER PACIENTE POR NOMBRE
    @Test
    void getPatientByExistentFullName_ShouldReturnPatientsWithMatchingName() {
        Patient patient = new PatientBuilder().build();
        String name = patient.getName();
        Patient patient2 = new PatientBuilder()
                .withId(2L)
                .withName(name)
                .withLastName("Gómez")
                .withDni("87654321")
                .build();

        Patient patient3 = new PatientBuilder()
                .withId(3L)
                .withName(name)
                .withLastName("López")
                .withDni("45678912")
                .build();
        List<Patient> patients = List.of(patient,patient2,patient3);

        when(this.patientRepository.findByName(name)).thenReturn(patients);

        List<Patient> result = this.patientService.getByName(name);
        assertEquals(patients.size(), result.size(), "La cantidad de pacientes devueltos debe coincidir");

        for (int i = 0; i < patients.size(); i++) {
            assertPatientEquals(patients.get(i), result.get(i));
        }
        verify(this.patientRepository).findByName(name);
    }

    @Test
    void getPatientByNonExistentFullName_ShouldReturnEmptyList() {
        String name = "Ana";
        when(this.patientRepository.findByName(name)).thenReturn(Collections.emptyList());

        List<Patient> result = this.patientService.getByName(name);
        assertTrue(result.isEmpty());
        verify(this.patientRepository).findByName(name);
    }

    //TEST OBTENER PACIENTE POR OBRA SOCIAL
    @Test
    void getPatientByExistentHealthInsuranceProvider_ShouldReturnPagedList() {
        String healthInsuranceProvider = "Swiss Medical";
        int page = 1;
        int size = 5;

        Patient patient = new PatientBuilder().build();
        patient.setHealthInsuranceProvider(healthInsuranceProvider);
        Patient patient2 = new PatientBuilder().build();
        patient2.setDni("23456453");
        patient2.setHealthInsuranceProvider(healthInsuranceProvider);
        List<Patient> expectedPatients = List.of(patient, patient2);

        when(this.patientRepository.findByHealthInsuranceProvider(healthInsuranceProvider, size, page * size))
                .thenReturn(expectedPatients);

        List<Patient> result = this.patientService.getByHealthInsuranceProvider(healthInsuranceProvider, page, size);

        assertNotNull(result, "La lista de pacientes no debe ser nula");
        assertEquals(expectedPatients.size(), result.size(), "Cantidad de pacientes devueltos incorrecta");

        for (int i = 0; i < expectedPatients.size(); i++) {
            assertPatientEquals(expectedPatients.get(i), result.get(i));
            assertEquals(healthInsuranceProvider, result.get(i).getHealthInsuranceProvider(),
                    "El paciente debe tener el proveedor de salud esperado");
        }

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

        assertNotNull(result, "La lista de pacientes no debe ser nula");
        assertTrue(result.isEmpty(), "La lista debe estar vacía cuando no hay pacientes con ese proveedor");

        verify(this.patientRepository).findByHealthInsuranceProvider(healthInsuranceProvider, size, 0);
    }

    @Test
    void getPatientByHealthInsuranceProviderWithExcessiveSizeSet_ShouldLimitSizeAndReturnPatients() {
        String healthInsurance = "OSDE";
        int page = 0;
        int requestedSize = 1000;
        int maxAllowedSize = 30;   // valor máximo establecido en el servicio

        List<Patient> patients = IntStream.range(0, maxAllowedSize)
                .mapToObj(i -> new PatientBuilder().withId((long) i).build())
                .collect(Collectors.toList());

        when(this.patientRepository.findByHealthInsuranceProvider(healthInsurance, maxAllowedSize, 0))
                .thenReturn(patients);

        List<Patient> result =  this.patientService.getByHealthInsuranceProvider(healthInsurance, page, requestedSize);

        assertNotNull(result, "La lista no debe ser nula");
        assertEquals(maxAllowedSize, result.size(), "La lista debe contener el máximo permitido de pacientes");

        verify(this.patientRepository).findByHealthInsuranceProvider(healthInsurance, maxAllowedSize, 0);
    }

    //TEST ACTUALIZAR POR ID
    @Test
    void updatePatientByValidIdWithFullUpdate_ShouldReplaceAllFields() {
        Long id = 1L;
        Patient existingPatient = new PatientBuilder()
                .withId(id)
                .build();

        Patient updatedPatient = new PatientBuilder()
                .withId(id)
                .withName("Analía")
                .withLastName("Gómez")
                .withInsurance("OSDE")
                .withEmail("nuevo_mail@example.com")
                .withPhone("47563765")
                .build();

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));
        when(this.patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        Patient result = this.patientService.updateById(id, updatedPatient);

        assertPatientEquals(updatedPatient, result);

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository).save(any(Patient.class));
    }

    @Test
    void updatePatientByValidIdWithPartialUpdate_ShouldUpdateOnlyProvidedFields() {
        long id = 1L;

        // Paciente original con datos completos
        Patient patientOriginal = new PatientBuilder()
                .withId(id)
                .withName("Ana")
                .withLastName("Torres")
                .withDni("35784627")
                .withInsurance(null)
                .withEmail(null)
                .withPhone(null)
                .build();

        // Datos que se quieren actualizar (algunos null)
        Patient updatedPatient = new PatientBuilder()
                .withId(id)
                .withName(null)
                .withLastName(null)
                .withDni(null)
                .withInsurance("Swiss Medical")
                .withEmail("ana_torres@example.com")
                .withPhone("1188749873")
                .build();

        // Paciente esperado después de la actualización:
        // conserva nombre, apellido y dni originales, actualiza los otros campos
        Patient patientFinal = new PatientBuilder()
                .withId(id)
                .withName("Ana")
                .withLastName("Torres")
                .withDni("35784627")
                .withInsurance("Swiss Medical")
                .withEmail("ana_torres@example.com")
                .withPhone("1188749873")
                .build();

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(patientOriginal));
        when(this.patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient result = this.patientService.updateById(id, updatedPatient);

        assertPatientEquals(patientFinal, result);

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository).save(any(Patient.class));
    }

    @Test
    void updatePatientByIdButNotFound_ShouldThrowPatientNotFoundException() {
        Long id = 1L;
        Patient updateRequest = new PatientBuilder()
                .withId(id)
                .build();

        when(this.patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> this.patientService.updateById(id, updateRequest));

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository, never()).save(any());
    }

    @Test
    void updatePatientByIdWithDuplicateDni_ShouldThrowPatientDniAlreadyInUse() {
        Long id = 1L;
        Patient patient = new PatientBuilder()
                .withId(id)
                .withDni("39847562")
                .build();

        // Paciente que ya tiene el DNI que se quiere utilizar
        Patient patient2 = new PatientBuilder()
                .withId(2L)
                .withDni("12345678")
                .build();

        // UpdateRequest con el DNI duplicado
        Patient updateRequest = new PatientBuilder()
                .withId(id)
                .withDni("12345678")
                .build();

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(this.patientRepository.findByDni(updateRequest.getDni())).thenReturn(Optional.of(patient2));

        assertThrows(PatientDniAlreadyInUseException.class,
                () -> this.patientService.updateById(id, updateRequest),
                "Se esperaba PatientDniAlreadyInUseException");

        verify(this.patientRepository).findById(id);
        verify(this.patientRepository).findByDni(updateRequest.getDni());
        verify(this.patientRepository, never()).save(any());
    }

    //TEST BORRAR POR ID
    @Test
    void deleteByValidId_ShouldDeletePatient() {
        Long id = 1L;
        Patient existingPatient = new Patient();
        existingPatient.setId(id);

        when(this.patientRepository.findById(id)).thenReturn(Optional.of(existingPatient));

        this.patientService.deleteById(id);

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
        Patient patient1 = new PatientBuilder().withId(13L).withName("Roberto").withLastName("Torres")
                .withDni("58473659").withInsurance("SwissMedical")
                .withEmail("roberto.torres@example.com").withPhone("1188749873").build();
        List<Patient> patients = List.of(patient1);

        when(exporterFactoryProvider.getFactory(anyString())).thenReturn(exporterFactory);
        when(exporterFactory.createPatientExporter()).thenReturn(patientExporter);
        when(this.patientRepository.findAll(null, null)).thenReturn(patients);

        String exportedData = "id,nombre,apellido,dni,obra_social,email,telefono\n13,Roberto,Torres,58473659,SwissMedical,roberto.torres@example.com,1123556766";
        when(this.patientExporter.export(patients)).thenReturn(exportedData);

        String result = patientService.exportPatients(format);

        assertEquals(exportedData, result, "El resultado de exportación debe coincidir con los datos esperados");
        verify(this.patientRepository).findAll(null, null);
        verify(this.exporterFactoryProvider).getFactory(format);
        verify(this.exporterFactory).createPatientExporter();
        verify(this.patientExporter).export(patients);
    }

    @Test
    void exportPatientsWithEmptyList_ShouldStillCallExporter() {
        String format = "csv";
        List<Patient> patients = new ArrayList<>();

        when(exporterFactoryProvider.getFactory(anyString())).thenReturn(exporterFactory);
        when(exporterFactory.createPatientExporter()).thenReturn(patientExporter);
        when(this.patientRepository.findAll(null, null)).thenReturn(patients);
        when(this.patientExporter.export(patients)).thenReturn("id,nombre,apellido,dni,obra_social,email,telefono\n");

        String result = patientService.exportPatients(format);

        assertEquals("id,nombre,apellido,dni,obra_social,email,telefono\n", result,
                "La exportación de una lista vacía debe devolver solo el header");
        verify(this.patientRepository).findAll(null, null);
        verify(this.exporterFactoryProvider).getFactory(format);
        verify(this.exporterFactory).createPatientExporter();
        verify(this.patientExporter).export(patients);
    }

    @Test
    void exportPatientsWithNotSupportedFormat_ShouldThrowException() {
        String invalidFormat = "jpg";

        when(this.patientRepository.findAll(null, null)).thenReturn(List.of());
        when(this.exporterFactoryProvider.getFactory(invalidFormat))
                .thenThrow(new ExporterTypeNotSupportedException(invalidFormat));

        ExporterTypeNotSupportedException exception = assertThrows(ExporterTypeNotSupportedException.class,
                () -> this.patientService.exportPatients(invalidFormat),
                "Se esperaba un ExporterTypeNotSupportedException");

        assertTrue(exception.getMessage().contains("Tipo de exportación ingresado 'jpg' no soportado"),
                "El mensaje no coincide");
        verify(this.patientRepository).findAll(null,null);
        verify(this.exporterFactoryProvider).getFactory(invalidFormat);
    }
}