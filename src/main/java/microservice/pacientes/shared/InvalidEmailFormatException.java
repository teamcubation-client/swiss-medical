package microservice.pacientes.shared;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(String message) {
        super("Formato de mail invalido");
    }
}
