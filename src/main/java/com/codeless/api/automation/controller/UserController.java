package com.codeless.api.automation.controller;

import com.codeless.api.automation.dto.UserRegistration;
import com.codeless.api.automation.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public void register(@Valid @RequestBody UserRegistration userRegistration) {
    userService.saveUser(userRegistration);
  }
}
