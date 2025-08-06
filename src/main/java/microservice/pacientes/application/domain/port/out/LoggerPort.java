package microservice.pacientes.application.domain.port.out;

public interface LoggerPort {

    void info(String message,Object... args );
    void debug(String message,Object... args );
    void warn(String message,Object... args );
    void error(String message,Object... args );
}
