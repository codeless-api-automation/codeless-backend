package com.codeless.api.automation.controller.security;

import com.codeless.api.automation.dto.AppUser;
import com.codeless.api.automation.dto.View;
import com.codeless.api.automation.service.security.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
  @JsonView(View.ALLOWED.class)
  public AppUser register(@Valid @RequestBody AppUser appUser) {
    return userService.saveUser(appUser);
  }
}
