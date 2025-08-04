package microservice.pacientes.shared;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String mensaje, LocalDateTime timestamp) {}