package com.swissmedical.patients.infrastructure.adapter.in.rest.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

  private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  @Around("within(com.swissmedical.patients.infrastructure.adapter.in.rest.controller.PatientController)")
  public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
    String clase = joinPoint.getSignature().getDeclaringTypeName();
    String metodo = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    String message = args == null || args.length == 0
            ? String.format("[PROXY] Endpoint llamado: %s.%s sin argumentos", clase, metodo)
            : String.format("[PROXY] Endpoint llamado: %s.%s con argumentos: %s", clase, metodo, Arrays.toString(args));

    logger.info(message);

    return joinPoint.proceed();
  }

  @AfterThrowing(pointcut = "execution(* com.swissmedical.patients.*.*(..))", throwing = "ex")
  public void logAfterThrowing(Exception ex) {
    logger.error("[PROXY] Excepción capturada: {}", ex.getMessage(), ex);
  }

  @Around("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
  public Object handleControllerException(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (Exception ex) {
      String clase = joinPoint.getSignature().getDeclaringTypeName();
      String metodo = joinPoint.getSignature().getName();
      logger.error("[PROXY] Excepción en el endpoint: {}.{} - Mensaje: {}", clase, metodo, ex.getMessage(), ex);
      throw ex; // Re-throw the exception to let it propagate
    }
  }
}
