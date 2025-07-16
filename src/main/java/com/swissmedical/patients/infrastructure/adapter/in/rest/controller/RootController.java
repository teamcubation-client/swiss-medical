package com.swissmedical.patients.infrastructure.adapter.in.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

  @GetMapping("/")
  public String redirectToApiDocs() {
    return "redirect:/swagger-ui/index.html";
  }
}
