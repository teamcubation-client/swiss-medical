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
  public Object logAroundController(ProceedingJoinPoint pjp) throws Throwable {
    String clase = pjp.getSignature().getDeclaringTypeName();
    String metodo = pjp.getSignature().getName();
    Object[] args = pjp.getArgs();

    if (!clase.contains("PatientController")) {
      return pjp.proceed();
    }

    if (args.length == 0) {
      logger.info("[PROXY] Endpoint llamado: /{} sin argumentos", metodo);
      return pjp.proceed();
    }

    logger.info("[PROXY] Endpoint llamado: /{} con argumentos: {}", metodo, Arrays.toString(args));

    return pjp.proceed();
  }
}
