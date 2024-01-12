package com.codeless.api.automation.controller;

import static com.codeless.api.automation.util.RestApiConstant.HEALTH_RESOURCE;

import com.codeless.api.automation.dto.Health;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(HEALTH_RESOURCE)
@Validated
@RequiredArgsConstructor
public class HealthController {

  @GetMapping
  public Health getHealth() {
    return Health.builder()
        .status("UP")
        .build();
  }

}
