package microservice.pacientes.unit.application.domain.validator.rules;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.application.domain.validator.rules.ApellidoValidatorRule;
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
@DisplayName("ApellidoValidatorRule Tests")
public class ApellidoValidatorRuleTest {
    private ApellidoValidatorRule apellidoValidatorRule;

    @BeforeEach
    void setUp() {
        apellidoValidatorRule = new ApellidoValidatorRule();
    }

    @Test
    @DisplayName("Debería validar un apellido válido")
    void shouldValidateSuccessfully_whenApellidoIsValid() {
        Paciente paciente = Paciente.builder().apellido("Perez").build();
        assertDoesNotThrow(() -> apellidoValidatorRule.validate(paciente));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Perez!@#",
            "1 ",
            "1 2345678",
            "1 23456789",
            "",
            "Ju an",
            "J1an",
            "$#!@-.",
            "   ",
            " "
    })
    @DisplayName("Debería lanzar excepción para un apellido inválido")
    void shouldThrowException_whenApellidoHasSpecialCharacters(String invalidApellido) {
        Paciente paciente = Paciente.builder().apellido(invalidApellido).build();
        PacienteArgumentoInvalido exception = assertThrows(PacienteArgumentoInvalido.class, () -> apellidoValidatorRule.validate(paciente));
        assertEquals("El apellido del paciente es inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Debería lanzar excepción para apellido nulo")
    void shouldThrowException_whenApellidoIsNull() {
        Paciente paciente = Paciente.builder().apellido(null).build();
        assertThrows(NullPointerException.class, () -> apellidoValidatorRule.validate(paciente));
    }

}
