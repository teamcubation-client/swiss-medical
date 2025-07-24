package com.teamcubation.api.pacientes.shared.exception;

public class JsonExportException extends RuntimeException {

    private static final String JSON_EXPORT_SERIALIZATION_ERROR = "Error al serializar %s a JSON";

    public JsonExportException(String target, Throwable cause) {
        super(String.format(JSON_EXPORT_SERIALIZATION_ERROR, target), cause);
    }
}
