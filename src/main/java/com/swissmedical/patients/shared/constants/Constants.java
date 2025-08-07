package com.swissmedical.patients.shared.constants;

public final class Constants {

    private Constants() { }

    public static final String LASTNAME_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$";
    public static final String DNI_REGEX = "\\d{8}";
    public static final String FIRSTNAME_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+$";
    public static final String PHONENUMBER_REGEX = "^\\d{3}-\\d{4}$";
    public static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

}
