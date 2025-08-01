package microservice.pacientes.shared;

public class InvalidEmailFormatException extends RuntimeException {
    public static final String MESSAGE = "Formato de mail invalido";
    public InvalidEmailFormatException(String message) {
        super(MESSAGE);
    }
}
