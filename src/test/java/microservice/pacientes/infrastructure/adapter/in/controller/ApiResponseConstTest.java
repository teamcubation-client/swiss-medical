package microservice.pacientes.infrastructure.adapter.in.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiResponseConstTest {
    @Test
    void codeConstants() {
        assertEquals("200", ApiResponseConst.CODE_OK);
        assertEquals("201", ApiResponseConst.CODE_CREATED);
        assertEquals("204", ApiResponseConst.CODE_NO_CONTENT);
        assertEquals("404", ApiResponseConst.CODE_NOT_FOUND);
        assertEquals("500", ApiResponseConst.CODE_INTERNAL_SERVER_ERROR);
    }

    @Test
    void descriptionConstants() {
        assertEquals("Paciente encontrado exitosamente", ApiResponseConst.DESC_OK);
        assertEquals("Paciente creado exitosamente", ApiResponseConst.DESC_CREATED);
        assertEquals("Paciente listado exitosamente", ApiResponseConst.DESC_LIST);
        assertEquals("Paciente actualizado exitosamente", ApiResponseConst.DESC_UPDATED);
        assertEquals("Paciente eliminado exitosamente", ApiResponseConst.DESC_DELETED);
        assertEquals("Paciente activado exitosamente", ApiResponseConst.DESC_ACTIVATED);
        assertEquals("Paciente Desactivado exitosamente", ApiResponseConst.DESC_DEACTIVATED);
        assertEquals("No existe el paciente con el ID", ApiResponseConst.DESC_NOT_FOUND_ID);
        assertEquals("No existe el paciente con el DNI", ApiResponseConst.DESC_NOT_FOUND_DNI);
        assertEquals("No existe el paciente con el nombre", ApiResponseConst.DESC_NOT_FOUND_NAME);
        assertEquals("No existe el paciente con la obra social", ApiResponseConst.DESC_NOT_FOUND_OBRASOCIAL);
        assertEquals("Error del sistema", ApiResponseConst.DESC_INTERNAL_ERROR);
        assertEquals("Lista de pacientes activos", ApiResponseConst.DESC_LIST_ACTIVATED);
        assertEquals("Lista de pacientes inactivos", ApiResponseConst.DESC_LIST_DEACTIVATED);
    }
}
