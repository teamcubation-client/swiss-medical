package microservice.pacientes.unit.application.domain.validator.rules;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.rules.TelefonoValidatorRule;
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
@DisplayName("TelefonoValidatorRule Tests")
public class TelefonoValidatorRuleTest {

    private TelefonoValidatorRule telefonoValidatorRule;

    @BeforeEach
    void setUp() {
        telefonoValidatorRule = new TelefonoValidatorRule();
    }

    @Test
    @DisplayName("Debería validar un teléfono válido de 9 dígitos")
    void shouldValidateSuccessfully_whenTelefonoIsValid() {
        Paciente paciente = Paciente.builder().telefono("123456789").build();
        assertDoesNotThrow(() -> telefonoValidatorRule.validate(paciente));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "111",
            "1 ",
            "1 2345678",
            "1 23456789",
            "1$2345678",
            "1a2345678",
            "",
            "         ",
            " ",
            "1234567890"

    })
    @DisplayName("Debería lanzar excepción para teléfono inválido")
    void shouldThrowException_whenTelefonoIsInvalid(String invalidTelefono) {
        Paciente paciente = Paciente.builder().telefono(invalidTelefono).build();
        PacienteArgumentoInvalido exception = assertThrows(PacienteArgumentoInvalido.class, () -> telefonoValidatorRule.validate(paciente));
        assertEquals("El teléfono del paciente es inválido", exception.getMessage());
    }


    @Test
    @DisplayName("Debería lanzar excepción para teléfono nulo")
    void shouldThrowException_whenTelefonoIsNull() {
        Paciente paciente = Paciente.builder().telefono(null).build();
        assertThrows(NullPointerException.class, () -> telefonoValidatorRule.validate(paciente));
    }
}
