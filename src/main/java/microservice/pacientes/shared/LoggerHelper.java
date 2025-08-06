package microservice.pacientes.shared;

import microservice.pacientes.application.domain.port.out.LoggerPort;

public class LoggerHelper {

    public static void info(LoggerPort logger, Object caller, String descripcion, Object... args) {
        String clase = caller.getClass().getSimpleName();
        String metodo = obtenerMetodoLlamador();
        logger.info("Clase: {}, Metodo: {}, Descripcion: " + descripcion, clase, metodo, args);
    }

    public static void warn(LoggerPort logger, Object caller, String descripcion, Object... args) {
        String clase = caller.getClass().getSimpleName();
        String metodo = obtenerMetodoLlamador();
        logger.warn("Clase: {}, Metodo: {}, Advertencia: " + descripcion, clase, metodo, args);
    }

    public static void error(LoggerPort logger, Object caller, String descripcion, Object... args) {
        String clase = caller.getClass().getSimpleName();
        String metodo = obtenerMetodoLlamador();
        logger.error("Clase: {}, Metodo: {}, Error: " + descripcion, clase, metodo, args);
    }

    public static void debug(LoggerPort logger, Object caller, String descripcion, Object... args) {
        String clase = caller.getClass().getSimpleName();
        String metodo = obtenerMetodoLlamador();
        logger.debug("Clase: {}, Metodo: {}, Debug: " + descripcion, clase, metodo, args);
    }

    public static void entrada(LoggerPort logger, Object caller) {
        String clase = caller.getClass().getSimpleName();
        String metodo = obtenerMetodoLlamador();
        logger.info("Entrada a Clase: {}, Metodo: {}", clase, metodo);
    }

    private static String obtenerMetodoLlamador() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace.length > 3 ? stackTrace[3].getMethodName() : "MetodoDesconocido";
    }
}