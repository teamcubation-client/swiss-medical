package com.swissmedical.patients.shared.utils;

public class ErrorMessages {

  public static final String LIMIT_OFFSET_INVALID = "Limit must be greater than 0 and offset must be non-negative.";
  public static final String PATIENT_NOT_FOUND_BY_ID = "No patient found with ID %s.";
  public static final String PATIENT_NOT_FOUND_BY_NAME = "No patients found with name %s.";
  public static final String PATIENT_NOT_FOUND_BY_DNI = "No patient found with DNI %s.";
  public static final String PATIENT_NOT_FOUND_BY_SOCIAL_SECURITY = "No patients found with social security %s.";
  public static final String PATIENT_DNI_DUPLICATE = "Patient with DNI %s already exists.";
  public static final String PATIENT_EMAIL_DUPLICATE = "Patient with email %s already exists.";

}
