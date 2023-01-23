package com.codeless.api.automation.controller;

import com.codeless.api.automation.configuration.EmailConfiguration;
import com.codeless.api.automation.dto.UserRegistration;
import com.codeless.api.automation.entity.User;
import com.codeless.api.automation.service.EmailService;
import com.codeless.api.automation.service.UserService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final EmailService emailService;
  private final EmailConfiguration emailConfiguration;

  @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public void register(@Valid @RequestBody UserRegistration userRegistration) {
    User user = userService.saveUser(userRegistration);
    emailService.sendEmail(user);
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<String> verify(
      @RequestParam("verification-token") String verificationToken) {
    User user = userService.verifyUser(verificationToken);
    if (!user.isEnabled()) {
      emailService
          .sendEmail(user);
      return ResponseEntity
          .status(HttpStatus.GONE)
          .body("Verification token is expired, please check your email to verify account");
    }
    return ResponseEntity
        .status(HttpStatus.PERMANENT_REDIRECT)
        .location(URI.create(emailConfiguration.getVerificationSuccessRedirect()))
        .body("User verified successfully.");
  }
}
