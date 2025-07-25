package microservice.pacientes.application.domain.validator.rules;

import microservice.pacientes.application.domain.model.Paciente;
import microservice.pacientes.shared.exception.PacienteArgumentoInvalido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@DisplayName("NombreValidatorRule Tests")
public class NombreValidatorRuleTest {
    private NombreValidatorRule nombreValidatorRule;

    @BeforeEach
    void setUp() {
        nombreValidatorRule = new NombreValidatorRule();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Juan",
            "María",
            "José",
            "Ñandú",
            "ANA",
            "ana"
    })
    @DisplayName("Debería validar un nombre válido")
    void validateValidNombre(String validNombre) {
        Paciente paciente = Paciente.builder().nombre(validNombre).build();
        assertDoesNotThrow(() -> nombreValidatorRule.validate(paciente));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Ju an",
            "J1an",
            "$#!@-.",
            "   ",
            " ",
            "",
            "Ju an",
            "J1an",
            "$#!@-.",
            "   ",
            " ",
            "Juan\n",
            "Juan1",
            "1Juan",
            "111"
    })
    @DisplayName("Debería lanzar excepción para nombres inválidos")
    void validateInvalidNombre(String invalidNombre) {
        Paciente paciente = Paciente.builder().nombre(invalidNombre).build();
        PacienteArgumentoInvalido exception = assertThrows(PacienteArgumentoInvalido.class, () -> nombreValidatorRule.validate(paciente));
        assertEquals("El nombre del paciente es inválido", exception.getMessage());
    }


    @Test
    @DisplayName("Debería lanzar excepción para nombre nulo")
    void validateNullNombre() {
        Paciente paciente = Paciente.builder().nombre(null).build();
        assertThrows(NullPointerException.class, () -> nombreValidatorRule.validate(paciente));
    }
}
