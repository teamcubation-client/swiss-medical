package microservice.pacientes.application.domain.validator;

public class PacienteValidator {
    public static boolean isValidDni(String dni) {
        return dni.matches("\\d{8}");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
    }

    public static boolean isValidNombre(String nombre){
        return nombre.matches("^[\\p{L}\\s]+$");
    }

    public static boolean isValidApellido(String apellido){
        return apellido.matches("^[\\p{L}\\s]+$");
    }

    public static boolean isValidTelefono(String telefono){
        return telefono.matches("\\d{9}");
    }

}
