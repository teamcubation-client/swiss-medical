package microservice.pacientes.application.domain.port.out;

public interface LoggerPort {

    void info(String message,Object... args );
    void debug(String message,Object... args );
    void warn(String message,Object... args );
    void error(String message,Object... args );

    default void entrada() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 2) {
            String className = stackTrace[2].getClassName();
            String methodName = stackTrace[2].getMethodName();
            String simpleClassName = className.substring(className.lastIndexOf('.') + 1);

            info("Ejecutando [{}.{}]", simpleClassName, methodName);
        }
    }
}
