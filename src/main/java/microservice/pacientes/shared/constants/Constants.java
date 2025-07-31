package microservice.pacientes.shared.constants;

public final class Constants {

    private Constants() { }

    public static final String APELLIDO_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$";
    public static final String DNI_REGEX = "\\d{8}";
    public static final String NOMBRE_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$";
    public static final String TELEFONO_REGEX = "\\d{9}";

}
