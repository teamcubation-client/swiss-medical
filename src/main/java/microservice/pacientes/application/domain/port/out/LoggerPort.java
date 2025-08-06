package microservice.pacientes.application.domain.port.out;

public interface LoggerPort {
    void setOrigin(String origin);
    void info(String message);
    void warn(String message);
    void error(String message);
}
