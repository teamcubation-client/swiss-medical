package com.teamcubation.api.pacientes.application.service;

import com.teamcubation.api.pacientes.application.domain.model.Patient;
import com.teamcubation.api.pacientes.application.domain.port.out.PatientExporterPortOut;
import com.teamcubation.api.pacientes.application.domain.port.out.PatientPortOut;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory.ExporterFactory;
import com.teamcubation.api.pacientes.infrastructure.adapter.in.exporter.factory.ExporterFactoryProvider;
import com.teamcubation.api.pacientes.shared.exception.DuplicatedPatientException;
import com.teamcubation.api.pacientes.shared.exception.ExporterTypeNotSupportedException;
import com.teamcubation.api.pacientes.shared.exception.PatientDniAlreadyInUseException;
import com.teamcubation.api.pacientes.shared.exception.PatientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
    private PatientPortOut patientPortOut;

    @Mock
    private ExporterFactoryProvider exporterFactoryProvider;

    @Mock
    private ExporterFactory exporterFactory;

    @Mock
    private PatientExporterPortOut patientExporter;

    @BeforeEach
    void setUp() {
        patientService = new PatientService(patientPortOut, exporterFactoryProvider);
    }

    /**
     * Se puede usar para utilizar paciente por defecto o crear nuevos pacientes
     */
    private static class TestPatientBuilder {
        private Long id;
        private String name = "Ana";
        private String lastName = "Torres";
        private String dni = "35784627";
        private String insurance = null;
        private String email = null;
        private String phone = null;

        TestPatientBuilder withId(Long id) { this.id = id; return this; }
        TestPatientBuilder withName(String name) { this.name = name; return this; }
        TestPatientBuilder withLastName(String lastName) { this.lastName = lastName; return this; }
        TestPatientBuilder withDni(String dni) { this.dni = dni; return this; }
        TestPatientBuilder withInsurance(String insurance) { this.insurance = insurance; return this; }
        TestPatientBuilder withEmail(String email) { this.email = email; return this; }
        TestPatientBuilder withPhone(String phone) { this.phone = phone; return this; }

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
                new TestPatientBuilder().withId(1L).build(),
                new TestPatientBuilder().withId(2L)
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

    @Nested
    class Create {
        @Test
        void createPatient_ShouldReturnPatientCreatedWithAllFieldsComplete() {
            Patient toCreate = new TestPatientBuilder()
                    .withEmail("anatorres@gmail.com")
                    .withPhone("1144773898")
                    .withInsurance("Swiss Medical")
                    .build();
            Patient saved = new TestPatientBuilder()
                    .withId(1L)
                    .withEmail("anatorres@gmail.com")
                    .withPhone("1144773898")
                    .withInsurance("Swiss Medical")
                    .build();

            when(patientPortOut.save(toCreate)).thenReturn(saved);

            Patient result = patientService.create(toCreate);
            assertPatientEquals(saved, result);
            verify(patientPortOut).save(toCreate);
        }

        @Test
        void createPatientWithRequiredFields_ShouldReturnPatientCreated(){
            Patient patient = new TestPatientBuilder().build();
            Patient savedPatient = new TestPatientBuilder().withId(1L).build();

            when(patientPortOut.save(patient)).thenReturn(savedPatient);

            Patient result = patientService.create(patient);
            assertPatientEquals(savedPatient, result);
            verify(patientPortOut).save(patient);
        }

        @Test
        void createPatientWithDuplicateDni_ShouldThrowException() {
            Patient patient = new TestPatientBuilder().build();

            when(patientPortOut.findByDni(patient.getDni()))
                    .thenReturn(Optional.of(new Patient()));

            DuplicatedPatientException exception = assertThrows(DuplicatedPatientException.class,
                    () -> patientService.create(patient)
            );

            assertTrue(exception.getMessage().contains("Ya existe un paciente con DNI: " + patient.getDni()),
                    "El mensaje de la excepción no coincide");
            verify(patientPortOut).findByDni(patient.getDni());
        }
    }

    @Nested
    class Get {
        @Test
        void getPatientsWithNullFilter_ShouldReturnAllPatients() {
            List<Patient> patients = defaultPatients();

            when(patientPortOut.findAll(null, null)).thenReturn(patients);

            List<Patient> result = patientService.getAll(null, null);

            assertEquals(patients, result, "La lista devuelta debe coincidir con la lista esperada de pacientes");
            verify(patientPortOut).findAll(null, null);
        }

        @Test
        void getPatientsWithEmptyFilter_ShouldReturnAllPatients() {
            List<Patient> patients = defaultPatients();

            when(patientPortOut.findAll("","")).thenReturn(patients);

            List<Patient> result = patientService.getAll("","");
            assertEquals(patients, result,
                    "La lista devuelta debe coincidir exactamente con la lista esperada de pacientes");
            verify(patientPortOut).findAll("","");
        }

        @Test
        void getPatientsWithNoResults_ShouldReturnEmptyList() {
            String dni = "34567898";
            when(patientPortOut.findAll(dni, null)).thenReturn(Collections.emptyList());

            List<Patient> result = patientService.getAll(dni, null);

            assertTrue(result.isEmpty(),
                    "La lista devuelta debe estar vacía cuando no hay pacientes que coincidan con el filtro");
            verify(patientPortOut).findAll(dni, null);
        }

        @Test
        void getPatientsWithDniFilter_ShouldReturnOnePatientWithDni() {
            Patient patient = new TestPatientBuilder().build();
            List<Patient> patients = List.of(patient);
            String dni = patient.getDni();

            when(patientPortOut.findAll(dni,null)).thenReturn(patients);
            List<Patient> result = patientService.getAll(dni,null);

            assertEquals(1, result.size(),
                    "Debe devolverse exactamente un paciente con el DNI: " + dni);

            assertTrue(result.stream().allMatch(p -> dni.equals(p.getDni())),
                    "El paciente devuelto debe tener el DNI esperado: " + dni);

            assertPatientEquals(patient, result.get(0));
            verify(patientPortOut).findAll(dni, null);
        }

        @Test
        void getPatientsWithNonexistentDniFiltered_ShouldReturnEmptyList() {
            String nonExistent = "23456789";
            when(patientPortOut.findAll(nonExistent,null)).thenReturn(Collections.emptyList());

            List<Patient> result = patientService.getAll(nonExistent,null);

            assertTrue(result.isEmpty(),
                    "Cuando no existen pacientes con el DNI especificado debe devolver una lista vacía");
            verify(patientPortOut).findAll(nonExistent,null);
        }

        @Test
        void getPatientsWithFullNameFilter_ShouldReturnMatchingPatients() {
            Patient juan1 = new TestPatientBuilder().withId(1L).withName("Juan").withLastName("Pérez").withDni("12345678").build();
            Patient juan2 = new TestPatientBuilder().withId(2L).withName("juan").withLastName("García").withDni("87654321").build();
            Patient juan3 = new TestPatientBuilder().withId(3L).withName("JUAN").withLastName("López").withDni("23456789").build();

            List<Patient> patientsWithSameName = List.of(juan1, juan2, juan3);

            when(patientPortOut.findAll(null, "Juan")).thenReturn(patientsWithSameName);

            List<Patient> result = patientService.getAll(null, "Juan");

            assertEquals(patientsWithSameName.size(), result.size(),
                    "El número de pacientes devueltos no coincide con la lista esperada");

            assertTrue(result.stream().allMatch(p -> p.getName().equalsIgnoreCase("Juan")),
                    "Todos los pacientes devueltos deben tener nombre 'Juan', ignorando mayúsculas");

            verify(patientPortOut).findAll(null, "Juan");
        }

        @Test
        void getPatientsWithPartialNameFilter_ShouldReturnPatientsWithMatchFiltered() {
            Patient patient1 = new TestPatientBuilder()
                    .withId(1L).withName("Jorge").withLastName("Pérez").withDni("12345678").build();
            Patient patient2 = new TestPatientBuilder()
                    .withId(2L).withName("jonathan").withLastName("García").withDni("87654321").build();
            Patient patient3 = new TestPatientBuilder()
                    .withId(3L).withName("JOEL").withLastName("López").withDni("23456789").build();

            List<Patient> patientsWithMatchName = List.of(patient1, patient2, patient3);
            String nameFilter = "Jo";

            when(patientPortOut.findAll(null, nameFilter)).thenReturn(patientsWithMatchName);

            List<Patient> result = patientService.getAll(null, nameFilter);

            assertEquals(patientsWithMatchName.size(), result.size(),
                    "La cantidad de pacientes devuelta no coincide con la esperada para el filtro: " + nameFilter);

            assertTrue(result.stream().allMatch(
                            p -> p.getName().toLowerCase().startsWith(nameFilter.toLowerCase())),
                    "Todos los pacientes devueltos deberían tener un nombre que comience con: " + nameFilter);

            verify(patientPortOut).findAll(null, nameFilter);
        }

        @Test
        void getPatientsWithNonexistentNameFiltered_ShouldReturnEmptyList() {
            String nameFilter = "Yoiner";

            when(patientPortOut.findAll(null, nameFilter))
                    .thenReturn(Collections.emptyList());

            List<Patient> result = patientService.getAll(null, nameFilter);

            assertTrue(result.isEmpty(),
                    "La lista debería estar vacía porque no existen pacientes con el nombre: " + nameFilter);

            verify(patientPortOut).findAll(null, nameFilter);
        }

        @Test
        void getPatientsWithFullDniAndPartialNameFiltered_ShouldReturnMatchingPatients() {
            String dniFilter = "47837645";
            String nameFilter = "Ju";

            Patient expectedPatient = new TestPatientBuilder()
                    .withId(2L)
                    .withName("Juan")
                    .withLastName("Pérez")
                    .withDni(dniFilter)
                    .build();

            List<Patient> patients = List.of(expectedPatient);

            when(patientPortOut.findAll(dniFilter, nameFilter)).thenReturn(patients);

            List<Patient> result = patientService.getAll(dniFilter, nameFilter);

            assertEquals(1, result.size(),
                    "Se esperaba exactamente un paciente para el filtro DNI: " + dniFilter + " y nombre: " + nameFilter);

            Patient actual = result.get(0);
            assertTrue(actual.getName().equalsIgnoreCase("Juan"),
                    "El nombre del paciente no coincide con el esperado (Juan) para el filtro: " + nameFilter);
            assertEquals(dniFilter, actual.getDni(),
                    "El DNI del paciente no coincide con el esperado: " + dniFilter);

            assertPatientEquals(expectedPatient, actual);

            verify(patientPortOut).findAll(dniFilter, nameFilter);
        }

        @Test
        void getPatientsWithNonexistentDniAndNameFiltered_ShouldReturnEmptyList() {
            String dni = "39284750";
            String name = "Ana";
            when(patientPortOut.findAll(dni,name)).thenReturn(Collections.emptyList());

            List<Patient> result = patientService.getAll(dni,name);
            assertTrue(result.isEmpty());
            verify(patientPortOut).findAll(dni,name);
        }
    }

    @Nested
    class GetPatientById {

        @Test
        void whenIdExists_ShouldReturnPatient() {
            Long id = 1L;

            Patient expectedPatient = new TestPatientBuilder()
                    .withId(id)
                    .withName("Ana")
                    .withLastName("Torres")
                    .withDni("35784627")
                    .withInsurance("Swiss Medical")
                    .withEmail("ana.torres@example.com")
                    .withPhone("1144773898")
                    .build();

            when(patientPortOut.findById(id)).thenReturn(Optional.of(expectedPatient));

            Patient result = patientService.getById(id);

            assertNotNull(result, "El paciente no debería ser nulo para el ID: " + id);
            assertPatientEquals(expectedPatient, result);

            verify(patientPortOut).findById(id);
        }

        @Test
        void whenIdDoesNotExist_ShouldThrowException() {
            long id = 5L;
            when(patientPortOut.findById(id)).thenReturn(Optional.empty());

            PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                    () -> patientService.getById(id)
            );

            assertTrue(exception.getMessage().contains("Paciente con ID: " + id + " no encontrado"));
            verify(patientPortOut).findById(id);
        }
    }

    @Nested
    class GetByDni {

        @Test
        void whenDniExists_ShouldReturnPatient() {
            String dni = "35784627";

            Patient expectedPatient = new TestPatientBuilder()
                    .withId(1L)
                    .withName("Ana")
                    .withLastName("Torres")
                    .withDni(dni)
                    .withInsurance("Swiss Medical")
                    .withEmail("ana.torres@example.com")
                    .withPhone("1144773898")
                    .build();

            when(patientPortOut.findByDni(dni)).thenReturn(Optional.of(expectedPatient));

            Patient result = patientService.getByDni(dni);

            assertNotNull(result, "El paciente no debería ser nulo para el DNI: " + dni);
            assertPatientEquals(expectedPatient, result);

            verify(patientPortOut).findByDni(dni);
        }

        @Test
        void whenDniDoesNotExist_ShouldThrowException() {
            String dni = "35784627";

            when(patientPortOut.findByDni(dni)).thenReturn(Optional.empty());

            PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                    () -> patientService.getByDni(dni)
            );

            assertTrue(exception.getMessage().contains("Paciente con DNI: " + dni + " no encontrado"),
                    "El mensaje de la excepción no coincide");

            verify(patientPortOut).findByDni(dni);
        }
    }

    @Nested
    class GetByName {
        @Test
        void getPatientByExistentFullName_ShouldReturnPatientsWithMatchingName() {
            Patient patient = new TestPatientBuilder().build();
            String name = patient.getName();
            Patient patient2 = new TestPatientBuilder()
                    .withId(2L)
                    .withName(name)
                    .withLastName("Gómez")
                    .withDni("87654321")
                    .build();

            Patient patient3 = new TestPatientBuilder()
                    .withId(3L)
                    .withName(name)
                    .withLastName("López")
                    .withDni("45678912")
                    .build();
            List<Patient> patients = List.of(patient,patient2,patient3);

            when(patientPortOut.findByName(name)).thenReturn(patients);

            List<Patient> result = patientService.getByName(name);
            assertEquals(patients.size(), result.size(), "La cantidad de pacientes devueltos debe coincidir");

            for (int i = 0; i < patients.size(); i++) {
                assertPatientEquals(patients.get(i), result.get(i));
            }
            verify(patientPortOut).findByName(name);
        }

        @Test
        void getPatientByNonExistentFullName_ShouldReturnEmptyList() {
            String name = "Ana";
            when(patientPortOut.findByName(name)).thenReturn(Collections.emptyList());

            List<Patient> result = patientService.getByName(name);
            assertTrue(result.isEmpty());
            verify(patientPortOut).findByName(name);
        }

    }

    @Nested
    class GetByHealthInsuranceProvider {
        @Test
        void getPatientByExistentHealthInsuranceProvider_ShouldReturnPagedList() {
            String healthInsuranceProvider = "Swiss Medical";
            int page = 1;
            int size = 5;

            Patient patient = new TestPatientBuilder().build();
            patient.setHealthInsuranceProvider(healthInsuranceProvider);
            Patient patient2 = new TestPatientBuilder().build();
            patient2.setDni("23456453");
            patient2.setHealthInsuranceProvider(healthInsuranceProvider);
            List<Patient> expectedPatients = List.of(patient, patient2);

            when(patientPortOut.findByHealthInsuranceProvider(healthInsuranceProvider, size, page * size))
                    .thenReturn(expectedPatients);

            List<Patient> result = patientService.getByHealthInsuranceProvider(healthInsuranceProvider, page, size);

            assertNotNull(result, "La lista de pacientes no debe ser nula");
            assertEquals(expectedPatients.size(), result.size(), "Cantidad de pacientes devueltos incorrecta");

            for (int i = 0; i < expectedPatients.size(); i++) {
                assertPatientEquals(expectedPatients.get(i), result.get(i));
                assertEquals(healthInsuranceProvider, result.get(i).getHealthInsuranceProvider(),
                        "El paciente debe tener el proveedor de salud esperado");
            }

            verify(patientPortOut).findByHealthInsuranceProvider(healthInsuranceProvider, size, page * size);
        }

        @Test
        void getPatientByNonexistentHealthInsuranceProvider_ShouldReturnEmptyList() {
            String healthInsuranceProvider = "Galeno";
            int page = 0;
            int size = 10;

            when(patientPortOut.findByHealthInsuranceProvider(healthInsuranceProvider, size, 0))
                    .thenReturn(Collections.emptyList());

            List<Patient> result = patientService.getByHealthInsuranceProvider(healthInsuranceProvider, page, size);

            assertNotNull(result, "La lista de pacientes no debe ser nula");
            assertTrue(result.isEmpty(), "La lista debe estar vacía cuando no hay pacientes con ese proveedor");

            verify(patientPortOut).findByHealthInsuranceProvider(healthInsuranceProvider, size, 0);
        }

        @Test
        void getPatientByHealthInsuranceProviderWithExcessiveSizeSet_ShouldLimitSizeAndReturnPatients() {
            String healthInsurance = "OSDE";
            int page = 0;
            int requestedSize = 1000;
            int maxAllowedSize = 30;   // valor máximo establecido en el servicio

            List<Patient> patients = IntStream.range(0, maxAllowedSize)
                    .mapToObj(i -> new TestPatientBuilder().withId((long) i).build())
                    .collect(Collectors.toList());

            when(patientPortOut.findByHealthInsuranceProvider(healthInsurance, maxAllowedSize, 0))
                    .thenReturn(patients);

            List<Patient> result =  patientService.getByHealthInsuranceProvider(healthInsurance, page, requestedSize);

            assertNotNull(result, "La lista no debe ser nula");
            assertEquals(maxAllowedSize, result.size(), "La lista debe contener el máximo permitido de pacientes");

            verify(patientPortOut).findByHealthInsuranceProvider(healthInsurance, maxAllowedSize, 0);
        }
    }

    @Nested
    class UpdateById {
        @Test
        void updatePatientByValidIdWithFullUpdate_ShouldReplaceAllFields() {
            Long id = 1L;
            Patient existingPatient = new TestPatientBuilder()
                    .withId(id)
                    .build();

            Patient updatedPatient = new TestPatientBuilder()
                    .withId(id)
                    .withName("Analía")
                    .withLastName("Gómez")
                    .withInsurance("OSDE")
                    .withEmail("nuevo_mail@example.com")
                    .withPhone("47563765")
                    .build();

            when(patientPortOut.findById(id)).thenReturn(Optional.of(existingPatient));
            when(patientPortOut.save(any(Patient.class))).thenReturn(updatedPatient);

            Patient result = patientService.updateById(id, updatedPatient);

            assertPatientEquals(updatedPatient, result);

            verify(patientPortOut).findById(id);
            verify(patientPortOut).save(any(Patient.class));
        }

        @Test
        void updatePatientByValidIdWithPartialUpdate_ShouldUpdateOnlyProvidedFields() {
            long id = 1L;

            // Paciente original con datos completos
            Patient patientOriginal = new TestPatientBuilder()
                    .withId(id)
                    .withName("Ana")
                    .withLastName("Torres")
                    .withDni("35784627")
                    .withInsurance(null)
                    .withEmail(null)
                    .withPhone(null)
                    .build();

            // Datos que se quieren actualizar (algunos null)
            Patient updatedPatient = new TestPatientBuilder()
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
            Patient patientFinal = new TestPatientBuilder()
                    .withId(id)
                    .withName("Ana")
                    .withLastName("Torres")
                    .withDni("35784627")
                    .withInsurance("Swiss Medical")
                    .withEmail("ana_torres@example.com")
                    .withPhone("1188749873")
                    .build();

            when(patientPortOut.findById(id)).thenReturn(Optional.of(patientOriginal));
            when(patientPortOut.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Patient result = patientService.updateById(id, updatedPatient);

            assertPatientEquals(patientFinal, result);

            verify(patientPortOut).findById(id);
            verify(patientPortOut).save(any(Patient.class));
        }

        @Test
        void updatePatientByIdButNotFound_ShouldThrowPatientNotFoundException() {
            Long id = 1L;
            Patient updateRequest = new TestPatientBuilder()
                    .withId(id)
                    .build();

            when(patientPortOut.findById(id)).thenReturn(Optional.empty());

            assertThrows(PatientNotFoundException.class,
                    () -> patientService.updateById(id, updateRequest));

            verify(patientPortOut).findById(id);
            verify(patientPortOut, never()).save(any());
        }

        @Test
        void updatePatientByIdWithDuplicateDni_ShouldThrowPatientDniAlreadyInUse() {
            Long id = 1L;
            Patient patient = new TestPatientBuilder()
                    .withId(id)
                    .withDni("39847562")
                    .build();

            // Paciente que ya tiene el DNI que se quiere utilizar
            Patient patient2 = new TestPatientBuilder()
                    .withId(2L)
                    .withDni("12345678")
                    .build();

            // UpdateRequest con el DNI duplicado
            Patient updateRequest = new TestPatientBuilder()
                    .withId(id)
                    .withDni("12345678")
                    .build();

            when(patientPortOut.findById(id)).thenReturn(Optional.of(patient));
            when(patientPortOut.findByDni(updateRequest.getDni())).thenReturn(Optional.of(patient2));

            assertThrows(PatientDniAlreadyInUseException.class,
                    () -> patientService.updateById(id, updateRequest),
                    "Se esperaba PatientDniAlreadyInUseException");

            verify(patientPortOut).findById(id);
            verify(patientPortOut).findByDni(updateRequest.getDni());
            verify(patientPortOut, never()).save(any());
        }
    }

    @Nested
    class DeleteById {
        @Test
        void deleteByValidId_ShouldDeletePatient() {
            Long id = 1L;
            Patient existingPatient = new Patient();
            existingPatient.setId(id);

            when(patientPortOut.findById(id)).thenReturn(Optional.of(existingPatient));

            patientService.deleteById(id);

            verify(patientPortOut).findById(id);
            verify(patientPortOut).deleteById(id);
        }

        @Test
        void deleteByNonExistentId_ShouldThrowPatientNotFoundException() {
            Long id = 1L;

            when(patientPortOut.findById(id)).thenReturn(Optional.empty());

            assertThrows(PatientNotFoundException.class,
                    () -> patientService.deleteById(id));

            verify(patientPortOut).findById(id);
            verify(patientPortOut, never()).deleteById(anyLong());
        }
    }

    @Nested
    class Export {
        @Test
        void exportPatients_ShouldReturnExportedData() {
            String format = "csv";
            Patient patient1 = new TestPatientBuilder().withId(13L).withName("Roberto").withLastName("Torres")
                    .withDni("58473659").withInsurance("SwissMedical")
                    .withEmail("roberto.torres@example.com").withPhone("1188749873").build();
            List<Patient> patients = List.of(patient1);

            when(exporterFactoryProvider.getFactory(anyString())).thenReturn(exporterFactory);
            when(exporterFactory.createPatientExporter()).thenReturn(patientExporter);
            when(patientPortOut.findAll(null, null)).thenReturn(patients);

            String exportedData = "id,nombre,apellido,dni,obra_social,email,telefono\n13,Roberto,Torres,58473659,SwissMedical,roberto.torres@example.com,1123556766";
            when(patientExporter.export(patients)).thenReturn(exportedData);

            String result = patientService.exportPatients(format);

            assertEquals(exportedData, result, "El resultado de exportación debe coincidir con los datos esperados");
            verify(patientPortOut).findAll(null, null);
            verify(exporterFactoryProvider).getFactory(format);
            verify(exporterFactory).createPatientExporter();
            verify(patientExporter).export(patients);
        }

        @Test
        void exportPatientsWithEmptyList_ShouldStillCallExporter() {
            String format = "csv";
            List<Patient> patients = new ArrayList<>();

            when(exporterFactoryProvider.getFactory(anyString())).thenReturn(exporterFactory);
            when(exporterFactory.createPatientExporter()).thenReturn(patientExporter);
            when(patientPortOut.findAll(null, null)).thenReturn(patients);
            when(patientExporter.export(patients)).thenReturn("id,nombre,apellido,dni,obra_social,email,telefono\n");

            String result = patientService.exportPatients(format);

            assertEquals("id,nombre,apellido,dni,obra_social,email,telefono\n", result,
                    "La exportación de una lista vacía debe devolver solo el header");
            verify(patientPortOut).findAll(null, null);
            verify(exporterFactoryProvider).getFactory(format);
            verify(exporterFactory).createPatientExporter();
            verify(patientExporter).export(patients);
        }

        @Test
        void exportPatientsWithNotSupportedFormat_ShouldThrowException() {
            String invalidFormat = "jpg";

            when(patientPortOut.findAll(null, null)).thenReturn(List.of());
            when(exporterFactoryProvider.getFactory(invalidFormat))
                    .thenThrow(new ExporterTypeNotSupportedException(invalidFormat));

            ExporterTypeNotSupportedException exception = assertThrows(ExporterTypeNotSupportedException.class,
                    () -> patientService.exportPatients(invalidFormat),
                    "Se esperaba un ExporterTypeNotSupportedException");

            assertTrue(exception.getMessage().contains("Tipo de exportación ingresado 'jpg' no soportado"),
                    "El mensaje no coincide");
            verify(patientPortOut).findAll(null,null);
            verify(exporterFactoryProvider).getFactory(invalidFormat);
        }
    }

}