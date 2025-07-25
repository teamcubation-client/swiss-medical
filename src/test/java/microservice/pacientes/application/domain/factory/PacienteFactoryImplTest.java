package microservice.pacientes.application.domain.factory;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.PacienteValidator;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("PacienteFactoryImpl Tests")
public class PacienteFactoryImplTest {

    @Mock
    private PacienteValidator pacienteValidator;

    @InjectMocks
    private PacienteFactoryImpl pacienteFactory;

    @Test
    void createValidPaciente() {
        String dni = "12345678";
        String nombre = "Juan";
        String apellido = "Perez";
        String obraSocial = "OSDE";
        String email = "juan.perez@gmail.com";
        String telefono = "123456789";

        Paciente paciente = pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);

        assertEquals(dni, paciente.getDni());
        assertEquals(nombre, paciente.getNombre());
        assertEquals(apellido, paciente.getApellido());
        assertEquals(obraSocial, paciente.getObraSocial());
        assertEquals(email, paciente.getEmail());
        assertEquals(telefono, paciente.getTelefono());
        verify(pacienteValidator).validate(paciente);
    }

    @Test
    void createPacienteWithInvalidDNI() {
        String dni = "1111";
        String nombre = "Juan";
        String apellido = "Perez";
        String obraSocial = "OSDE";
        String email = "juan.perez@gmail.com";
        String telefono = "123456789";
        PacienteArgumentoInvalido exception = new PacienteArgumentoInvalido("Argumento invalido");

        doThrow(exception).when(pacienteValidator).validate(any(Paciente.class));

        PacienteArgumentoInvalido pacienteArgumentoInvalido = assertThrows(PacienteArgumentoInvalido.class, () -> {
            pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);
        });
        assertEquals("Argumento invalido", pacienteArgumentoInvalido.getMessage());
        verify(pacienteValidator).validate(any(Paciente.class));
    }

    @Test
    void createPacienteWithInvalidNombre() {
        String dni = "12345678";
        String nombre = "";
        String apellido = "Perez";
        String obraSocial = "OSDE";
        String email = "juan.perez@gmail.com";
        String telefono = "123456789";

        doThrow(new PacienteArgumentoInvalido(""))
                .when(pacienteValidator)
                .validate(any(Paciente.class));

        assertThrows(PacienteArgumentoInvalido.class, () -> {
            pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);
        });
        verify(pacienteValidator).validate(any(Paciente.class));
    }

    @Test
    void createPacienteWithInvalidApellido() {
        String dni = "12345678";
        String nombre = "Juan";
        String apellido = "";
        String obraSocial = "OSDE";
        String email = "juan.perez@gmail.com";
        String telefono = "123456789";

        doThrow(new PacienteArgumentoInvalido(""))
                .when(pacienteValidator)
                .validate(any(Paciente.class));

        assertThrows(PacienteArgumentoInvalido.class, () -> {
            pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);
        });
        verify(pacienteValidator).validate(any(Paciente.class));
    }

    @Test
    void createPacienteWithInvalidEmail() {
        String dni = "12345678";
        String nombre = "Juan";
        String apellido = "Perez";
        String obraSocial = "OSDE";
        String email = "juan.perez";
        String telefono = "123456789";

        doThrow(new PacienteArgumentoInvalido(""))
                .when(pacienteValidator)
                .validate(any(Paciente.class));

        assertThrows(PacienteArgumentoInvalido.class, () -> {
            pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);
        });
        verify(pacienteValidator).validate(any(Paciente.class));
    }

    @Test
    void createPacienteWithInvalidTelefono() {
        String dni = "12345678";
        String nombre = "";
        String apellido = "Perez";
        String obraSocial = "OSDE";
        String email = "juan.perez";
        String telefono = "123456789";

        doThrow(new PacienteArgumentoInvalido(""))
                .when(pacienteValidator)
                .validate(any(Paciente.class));

        assertThrows(PacienteArgumentoInvalido.class, () -> {
            pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);
        });
        verify(pacienteValidator).validate(any(Paciente.class));
    }

    @Test
    void createPacienteInvalid() {
        String dni = "1";
        String nombre = "";
        String apellido = "";
        String obraSocial = "OSDE";
        String email = "juan.perez";
        String telefono = "123";

        doThrow(new PacienteArgumentoInvalido(""))
                .when(pacienteValidator)
                .validate(any(Paciente.class));

        assertThrows(PacienteArgumentoInvalido.class, () -> {
            pacienteFactory.create(dni, nombre, apellido, obraSocial, email, telefono);
        });
        verify(pacienteValidator).validate(any(Paciente.class));
    }
}
