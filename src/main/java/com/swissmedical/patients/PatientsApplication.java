package com.swissmedical.patients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class PatientsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PatientsApplication.class, args);
  }

}
