package com.tq.pacientes.infrastructure.logging;

import com.tq.pacientes.application.domain.model.Patient;
import com.tq.pacientes.application.domain.model.patient.processing.AdultPatientSave;
import com.tq.pacientes.application.domain.model.patient.processing.SeniorPatientSave;
import com.tq.pacientes.application.domain.model.patient.processing.YoungPatientSave;
import com.tq.pacientes.shared.util.PatientConstants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class PatientSaveLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(PatientSaveLoggingAspect.class);

    @Pointcut("execution(* com.tq.pacientes.application.domain.model.patient.processing.PatientSaveTemplate.*(..))")
    public void templateMethods() {}

    @Around("templateMethods()")
    public Object logAndHandleTemplateMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        Object result = joinPoint.proceed();

        if (args.length > 0 && args[0] instanceof Patient patient) {
            handleLogging(joinPoint, methodName, className, patient);
        }

        return result;
    }

    private void logNotification(String patientType, String email) {
        logger.info(PatientConstants.LOGGER_INFO_SEND_NOTIFICATION, patientType, email);
    }

    private void handleLogging(ProceedingJoinPoint joinPoint, String methodName, String className, Patient patient) {
        Object target = joinPoint.getTarget();

        if (className.contains("Young") && target instanceof YoungPatientSave young) {
            handleYoungLogging(methodName, patient, young);
        } else if (className.contains("Adult") && target instanceof AdultPatientSave adult) {
            handleAdultLogging(methodName, patient, adult);
        } else if (className.contains("Senior") && target instanceof SeniorPatientSave senior) {
            handleSeniorLogging(methodName, patient, senior);
        }
    }

    private void handleYoungLogging(String methodName, Patient patient, YoungPatientSave target) {
        int age = target.getCurrentPatientAge();

        if (PatientConstants.METHOD_VALIDATE.equals(methodName) && target.isUnder12WithoutInsurance(patient)) {
            logger.warn(PatientConstants.LOGGER_WARN_MESSAGE_PATIENT_UNDER_12_WITHOUT_HEALTH_INSURANCE, patient.getDni());
        }

        if (PatientConstants.METHOD_PRE_PROCESS.equals(methodName)) {
            if (target.requiresVaccineReminder(age)) {
                logger.info(PatientConstants.LOGGER_INFO_PEDIATRIC_VACCINE_REMINDERS, patient.getDni());
            } else if (target.requiresCheckupReminder(age)) {
                logger.info(PatientConstants.LOGGER_INFO_PEDIATRIC_CHECKUP_REMINDERS, patient.getDni());
            }
        }

        if (PatientConstants.METHOD_POST_PROCESS.equals(methodName)) {
            logNotification(PatientConstants.PATIENT_TYPE_YOUNG, patient.getEmail());
            String riskLevel = target.calculatePediatricRiskLevel(age);
            logger.info(PatientConstants.LOGGER_INFO_PEDIATRIC_RISK_LEVEL, patient.getDni(), riskLevel);
        }
    }

    private void handleAdultLogging(String methodName, Patient patient, AdultPatientSave target) {
        int age = target.getCurrentPatientAge();

        if (PatientConstants.METHOD_POST_PROCESS.equals(methodName)) {
            logNotification(PatientConstants.PATIENT_TYPE_ADULT, patient.getEmail());

            if (age >= PatientConstants.ADULT_AGE) {
                logger.info(PatientConstants.LOGGER_INFO_ADULT_CHECKUP_REMINDERS, patient.getDni());
            }

            if (age >= PatientConstants.ADULT_AGE_PLUS) {
                logger.info(PatientConstants.LOGGER_INFO_ADULT_CARDIO_SCREENING, patient.getDni());
            }

            if (age >= PatientConstants.ADULT_AGE_PLUS_PLUS) {
                logger.info(PatientConstants.LOGGER_INFO_ADULT_ONCO_SCREENING, patient.getDni());
            }
        }
    }

    private void handleSeniorLogging(String methodName, Patient patient, SeniorPatientSave target) {
        int age = target.getCurrentPatientAge();

        if (PatientConstants.METHOD_PRE_PROCESS.equals(methodName)) {
            String riskLevel = target.assessGeriatricComplexity(age);
            logger.info(PatientConstants.LOGGER_INFO_SENIOR_RISK_LEVEL, patient.getDni(), riskLevel);
        }

        if (PatientConstants.METHOD_POST_PROCESS.equals(methodName)) {
            logNotification(PatientConstants.PATIENT_TYPE_SENIOR, patient.getEmail());
        }
    }
}
