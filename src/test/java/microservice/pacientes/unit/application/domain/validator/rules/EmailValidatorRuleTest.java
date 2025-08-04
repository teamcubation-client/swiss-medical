package microservice.pacientes.unit.application.domain.validator.rules;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.rules.EmailValidatorRule;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailValidatorRule Tests")
public class EmailValidatorRuleTest {

    private EmailValidatorRule emailValidatorRule;

    @BeforeEach
    void setUp() {
        emailValidatorRule = new EmailValidatorRule();
    }

    @Test
    @DisplayName("Debería validar un email válido")
    void shouldValidateSuccessfully_whenEmailIsValid() {
        Paciente paciente = Paciente.builder().email("test@example.com").build();
        assertDoesNotThrow(() -> emailValidatorRule.validate(paciente));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@example.com",
            "Joe Smith <email@example.com>",
            "email.example.com",
            "email@example@example.com",
            ".email@example.com",
            "email.@example.com",
            "email..email@example.com",
            "email@example.com (Joe Smith)",
            "email@example",
            "email@example..com",
            "Abc..123@example.com",
            ""
    })
    @DisplayName("Debería lanzar excepción para emails inválidos")
    void shouldThrowException_whenEmailHasInvalidFormat(String invalidEmail) {
        Paciente paciente = Paciente.builder().email(invalidEmail).build();
        PacienteArgumentoInvalido exception = assertThrows(PacienteArgumentoInvalido.class, () -> emailValidatorRule.validate(paciente));
        assertEquals("El email del paciente es inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar excepción para email nulo")
    void shouldThrowException_whenEmailIsNull() {
        Paciente paciente = Paciente.builder().email(null).build();
        assertThrows(NullPointerException.class, () -> emailValidatorRule.validate(paciente));
    }
}
