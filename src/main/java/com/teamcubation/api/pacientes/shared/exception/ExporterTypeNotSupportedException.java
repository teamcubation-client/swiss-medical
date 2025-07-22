package com.teamcubation.api.pacientes.shared.exception;

public class ExporterTypeNotSupportedException extends RuntimeException {
    private static final String ERROR_TYPE_NOT_SUPPORTED = "Tipo de exportación ingresado '%s' no soportado";
    public ExporterTypeNotSupportedException(String type) {
        super(String.format(ERROR_TYPE_NOT_SUPPORTED, type));
    }
}