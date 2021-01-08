package com.codeless.api.automation.controller;

import com.codeless.api.automation.security.domain.User;
import com.codeless.api.automation.security.domain.View;
import com.codeless.api.automation.security.service.UserService;
import com.codeless.api.automation.security.service.impl.UserServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserServiceImpl userService) {
    this.userService = userService;
  }

  @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
  @JsonView(View.ALLOWED.class)
  public User register(@RequestBody @Valid User user) {
    return userService.saveUser(user);
  }
}
