//package microservice.pacientes.application.domain.validator;
//
//import microservice.pacientes.application.domain.model.Paciente;
//import microservice.pacientes.application.domain.validator.rules.PacienteValidatorRule;
//import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
//public class PacienteValidatorImplTest {
//
//    @Mock
//    private PacienteValidatorRule rule1;
//
//    @Mock
//    private PacienteValidatorRule rule2;
//
//    @Mock
//    private PacienteValidatorRule rule3;
//
//    @Mock
//    private PacienteValidatorRule rule4;
//
//    @Mock
//    private PacienteValidatorRule rule5;
//
//    private PacienteValidatorImpl pacienteValidator;
//
//    private List<PacienteValidatorRule> reglas;
//
//    @BeforeEach
//    void setUp() {
//        reglas = List.of(rule1, rule2, rule3, rule4, rule5);
//        pacienteValidator = new PacienteValidatorImpl(reglas);
//    }
//
//    @Test
//    void validateValidPaciente() {
//        Paciente paciente = Paciente.builder()
//                                .dni("12345678")
//                                .nombre("Juan")
//                                .apellido("Perez")
//                                .obraSocial("OSDE")
//                                .email("juan.perez@gmail.com")
//                                .telefono("123456789")
//                                .build();
//
//        pacienteValidator.validate(paciente);
//
//        for (PacienteValidatorRule regla : reglas) {
//            verify(regla).validate(paciente);
//        }
//    }
//
//    @Test
//    void validateInvalidPaciente() {
//        Paciente paciente = Paciente.builder()
//                                .dni("1")
//                                .nombre("Juan")
//                                .apellido("Perez")
//                                .obraSocial("OSDE")
//                                .email("juan.perez@gmail.com")
//                                .telefono("123456789")
//                                .build();
//
//        doThrow(new PacienteArgumentoInvalido(""))
//                .when(reglas.get(0))
//                .validate(any(Paciente.class));
//
//        assertThrows(PacienteArgumentoInvalido.class, () -> {
//            pacienteValidator.validate(paciente);
//        });
//        verify(reglas.get(0), times(1)).validate(paciente);
//    }
//}

package microservice.pacientes.application.domain.validator;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.rules.PacienteValidatorRule;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("PacienteValidatorImpl Tests")
class PacienteValidatorImplTest {

    @Mock
    private PacienteValidatorRule rule1;

    @Mock
    private PacienteValidatorRule rule2;

    @Mock
    private PacienteValidatorRule rule3;

    @Mock
    private Paciente paciente;

    private PacienteValidatorImpl validator;

    @BeforeEach
    void setUp() {
        List<PacienteValidatorRule> rules = Arrays.asList(rule1, rule2, rule3);
        validator = new PacienteValidatorImpl(rules);
    }

    @Test
    @DisplayName("Debería validar exitosamente cuando todas las reglas pasan")
    void validateValidPaciente() {
        doNothing().when(rule1).validate(paciente);
        doNothing().when(rule2).validate(paciente);
        doNothing().when(rule3).validate(paciente);

        assertDoesNotThrow(() -> validator.validate(paciente));
        verify(rule1, times(1)).validate(paciente);
        verify(rule2, times(1)).validate(paciente);
        verify(rule3, times(1)).validate(paciente);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la primera regla falla")
    void validateInvalidPacienteRule1() {
        PacienteArgumentoInvalido exception = new PacienteArgumentoInvalido("Argumento invalido en regla 1");
        doThrow(exception).when(rule1).validate(paciente);

        PacienteArgumentoInvalido pacienteArgumentoInvalido = assertThrows(
                PacienteArgumentoInvalido.class,
                () -> validator.validate(paciente)
        );

        assertEquals("Argumento invalido en regla 1", pacienteArgumentoInvalido.getMessage());
        verify(rule1, times(1)).validate(paciente);
        verify(rule2, never()).validate(paciente);
        verify(rule3, never()).validate(paciente);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la segunda regla falla")
    void validateInvalidPacienteRule2() {
        doNothing().when(rule1).validate(paciente);
        PacienteArgumentoInvalido exception = new PacienteArgumentoInvalido("Argumento invalido en regla 2");
        doThrow(exception).when(rule2).validate(paciente);

        PacienteArgumentoInvalido pacienteArgumentoInvalido = assertThrows(
                PacienteArgumentoInvalido.class,
                () -> validator.validate(paciente)
        );

        assertEquals("Argumento invalido en regla 2", pacienteArgumentoInvalido.getMessage());
        verify(rule1, times(1)).validate(paciente);
        verify(rule2, times(1)).validate(paciente);
        verify(rule3, never()).validate(paciente);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la última regla falla")
    void validateInvalidPacienteRule3() {
        doNothing().when(rule1).validate(paciente);
        doNothing().when(rule2).validate(paciente);
        PacienteArgumentoInvalido exception = new PacienteArgumentoInvalido("Argumento invalido en regla 3");
        doThrow(exception).when(rule3).validate(paciente);

        PacienteArgumentoInvalido pacienteArgumentoInvalido = assertThrows(
                PacienteArgumentoInvalido.class,
                () -> validator.validate(paciente)
        );

        assertEquals("Argumento invalido en regla 3", pacienteArgumentoInvalido.getMessage());
        verify(rule1, times(1)).validate(paciente);
        verify(rule2, times(1)).validate(paciente);
        verify(rule3, times(1)).validate(paciente);
    }

    @Test
    @DisplayName("Debería manejar correctamente una lista vacía de reglas")
    void validateValidPacienteWithoutRules() {
        PacienteValidatorImpl emptyValidator = new PacienteValidatorImpl(Collections.emptyList());

        assertDoesNotThrow(() -> emptyValidator.validate(paciente));
    }
}
