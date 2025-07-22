package com.swissmedical.patients.infrastructure.adapter.in.rest.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
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

  @Around("within(@org.springframework.web.bind.annotation.RestController *)")
  public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
    String clase = joinPoint.getSignature().getDeclaringTypeName();
    String metodo = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    if (!clase.contains("PatientController")) {
      return joinPoint.proceed();
    }

    if (args.length == 0) {
      logger.info("[PROXY] Endpoint llamado: /{} sin argumentos", metodo);
      return joinPoint.proceed();
    }

    logger.info("[PROXY] Endpoint llamado: /{} con argumentos: {}", metodo, Arrays.toString(args));

    return joinPoint.proceed();
  }
}
