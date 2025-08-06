package microservice.pacientes.infrastructure.adapter.out.logger;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import microservice.pacientes.application.domain.port.out.LoggerPort;

@Slf4j
@Setter
@Getter
public class LoggerImpl implements LoggerPort {
    public String origin;

    public LoggerImpl() {
        this.origin = "Generic";
    }

    @Override
    public void info(String message) {
        log.info("["+origin+"] - "+message);
    }

    @Override
    public void warn(String message) {
        log.warn("["+origin+"] - "+message);
    }

    @Override
    public void error(String message) {
        log.error("["+origin+"] - "+message);
    }
}
